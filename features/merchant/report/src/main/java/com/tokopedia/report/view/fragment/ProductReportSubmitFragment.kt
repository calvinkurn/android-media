package com.tokopedia.report.view.fragment

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker.common.*
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.data.util.MerchantReportTracking
import com.tokopedia.report.databinding.FragmentProductReportBinding
import com.tokopedia.report.di.MerchantReportComponent
import com.tokopedia.report.view.activity.ProductReportFormActivity
import com.tokopedia.report.view.activity.ReportInputDetailActivity
import com.tokopedia.report.view.adapter.ReportFormAdapter
import com.tokopedia.report.view.viewmodel.ProductReportSubmitViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class ProductReportSubmitFragment : BaseDaggerFragment() {
    private lateinit var adapter: ReportFormAdapter
    private var photoTypeSelected: String? = null
    override fun getScreenName(): String? = null
    private var dialogSubmit: DialogUnify? = null
    private var productId: String = "0"
    private val tracking by lazy { MerchantReportTracking() }
    private var binding by autoCleared<FragmentProductReportBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ProductReportSubmitViewModel

    override fun initInjector() {
        getComponent(MerchantReportComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val vmProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = vmProvider.get(ProductReportSubmitViewModel::class.java)
        }
        arguments?.let {
            productId = it.getString(ProductReportFormActivity.PRODUCT_ID, "0")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cacheId = arguments?.getString(ProductReportFormActivity.REASON_CACHE_ID, "") ?: ""
        val reason: ProductReportReason? = context?.let {
            val cacheManager = SaveInstanceCacheManager(it, cacheId)
            cacheManager.get(ProductReportFormActivity.REASON_OBJECT, ProductReportReason::class.java)

        }
        binding.recyclerView.clearItemDecoration()
        reason?.let {reasonItem ->
            val popupField = reasonItem.additionalFields.firstOrNull { additionalField -> additionalField.type == "popup" }
            if (popupField != null && activity != null){
                dialogSubmit = DialogUnify(requireActivity(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                    setTitle(popupField.value)
                    setDescription(popupField.detail)
                    setPrimaryCTAText(getString(R.string.label_report))
                    setSecondaryCTAText(getString(R.string.report_cancel))
                    setPrimaryCTAClickListener{
                        dismiss()
                        binding.loadingView.visible()
                        viewModel.submitReport(
                            productId.toLongOrZero(),
                            reasonItem.categoryId.toIntOrZero(),
                            adapter.inputs
                        )
                    }
                    setSecondaryCTAClickListener {
                        tracking.eventReportCancelDisclaimer(reasonItem.value.toLowerCase())
                        dismiss()
                    }
                }
            }

            adapter = ReportFormAdapter(reasonItem, tracking, this::openInputDetail,
                    this::openPhotoPicker, this::onSubmitClicked)
            binding.recyclerView.adapter = adapter
        }
    }

    private fun onSuccessSubmit(isSuccess: Boolean){
        tracking.eventReportLaporDisclaimer(adapter.trackingReasonLabel, isSuccess)
        binding.loadingView.gone()
        if (!isSuccess){
            view?.let {
                Toaster.showErrorWithAction(it, getString(R.string.fail_to_report),
                        Snackbar.LENGTH_LONG, getString(com.tokopedia.resources.common.R.string.general_label_ok), View.OnClickListener {})
            }
        } else {
            sendResult()
        }
    }

    private fun sendResult() {
        activity?.run {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun onFailSubmit(throwable: Throwable?){
        binding.loadingView.gone()
        tracking.eventReportLaporDisclaimer(adapter.trackingReasonLabel, false)
        view?.let {
            Toaster.showErrorWithAction(it, ErrorHandler.getErrorMessage(activity, throwable),
                    Snackbar.LENGTH_LONG, getString(com.tokopedia.resources.common.R.string.general_label_ok), View.OnClickListener {})
        }
    }

    private fun onSubmitClicked(){
        if (isInputValid){
            dialogSubmit?.show()
        }
    }

    private val isInputValid: Boolean
        get() {
            val total = adapter.itemCount - 1
            var valid = true
            for (i in 1..total){
                val holder = binding.recyclerView.findViewHolderForAdapterPosition(i-1)
                if ( holder is ReportFormAdapter.ValidateViewHolder)
                    valid = valid and holder.validate()
            }
            return valid
        }

    private fun openInputDetail(key: String, value: String, minChar: Int, maxChar: Int){
        photoTypeSelected = key
        context?.let {
            startActivityForResult(ReportInputDetailActivity.createIntent(it, value, minChar, maxChar), REQUEST_CODE_DETAIL_INPUT)
        }
    }

    private fun openPhotoPicker(photoType: String, maxPick: Int){
        this.photoTypeSelected = photoType
        context?.let {
            val builder = ImagePickerBuilder.getSquareImageBuilder(it)
                    .withSimpleEditor()
                    .withSimpleMultipleSelection(maxPick = maxPick).apply {
                        title = getString(R.string.report_choose_picture)
                    }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.IMAGE_PICKER)
            intent.putImagePickerBuilder(builder)
            intent.putParamPageSource(ImagePickerPageSource.PRODUCT_REPORT_PAGE)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_IMAGE){
            if (resultCode == Activity.RESULT_OK && data != null){
                tracking.eventReportAddPhotoOK(adapter.trackingReasonLabel)
                val imageUrlOrPathList = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
                if (imageUrlOrPathList.size > 0) {
                    photoTypeSelected?.let {
                        adapter.updatePhotoForType(it, imageUrlOrPathList)
                    }
                    photoTypeSelected = null
                }
            } else {
                tracking.eventReportAddPhotoFail(adapter.trackingReasonLabel)
            }
        } else if (resultCode == Activity.RESULT_OK && data != null){
            if (requestCode == REQUEST_CODE_DETAIL_INPUT){
                photoTypeSelected?.let {
                    val input = data.getStringExtra(ReportInputDetailFragment.INPUT_VALUE)
                    val isValid = data.getBooleanExtra(ReportInputDetailFragment.VALID_VALUE, false)
                    adapter.updateTextInput(it, input, isValid)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getSubmitResult().observe(viewLifecycleOwner, observerSubmitResult)
    }

    override fun onDestroy() {
        viewModel.flush()
        super.onDestroy()
    }

    private val observerSubmitResult = Observer<Result<Boolean>> {
        when (it) {
            is Success -> onSuccessSubmit(it.data)
            is Fail -> onFailSubmit(it.throwable)
        }
    }

    companion object {
        private const val REQUEST_CODE_IMAGE = 0x01
        private const val REQUEST_CODE_DETAIL_INPUT = 0x02

        fun createInstance(cacheId: String, productId: String) = ProductReportSubmitFragment().apply {
            arguments = Bundle().also {
                it.putString(ProductReportFormActivity.REASON_CACHE_ID, cacheId)
                it.putString(ProductReportFormActivity.PRODUCT_ID, productId)
            }
        }
    }
}