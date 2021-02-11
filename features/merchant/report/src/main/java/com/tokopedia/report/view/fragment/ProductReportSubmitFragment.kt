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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.data.util.MerchantReportTracking
import com.tokopedia.report.di.MerchantReportComponent
import com.tokopedia.report.view.activity.ProductReportFormActivity
import com.tokopedia.report.view.activity.ReportInputDetailActivity
import com.tokopedia.report.view.adapter.ReportFormAdapter
import com.tokopedia.report.view.customview.UnifyDialog
import com.tokopedia.report.view.viewmodel.ProductReportSubmitViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_product_report.*
import javax.inject.Inject

class ProductReportSubmitFragment : BaseDaggerFragment() {
    private lateinit var adapter: ReportFormAdapter
    private var photoTypeSelected: String? = null
    override fun getScreenName(): String? = null
    private var dialogSubmit: UnifyDialog? = null
    private var productId: String = "0"
    private val tracking by lazy { MerchantReportTracking() }

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
        return inflater.inflate(R.layout.fragment_product_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cacheId = arguments?.getString(ProductReportFormActivity.REASON_CACHE_ID, "") ?: ""
        val reason: ProductReportReason? = context?.let {
            val cacheManager = SaveInstanceCacheManager(it, cacheId)
            cacheManager.get(ProductReportFormActivity.REASON_OBJECT, ProductReportReason::class.java)

        }
        recycler_view.clearItemDecoration()
        reason?.let {reasonItem ->
            val popupField = reasonItem.additionalFields.firstOrNull { additionalField -> additionalField.type == "popup" }
            if (popupField != null && activity != null){
                dialogSubmit = UnifyDialog(activity!!, UnifyDialog.HORIZONTAL_ACTION, UnifyDialog.NO_HEADER).apply {
                    setTitle(popupField.value)
                    setDescription(popupField.detail)
                    setOk(getString(R.string.label_report))
                    setSecondary(getString(R.string.report_cancel))
                    setOkOnClickListner(View.OnClickListener {
                        dismiss()
                        loading_view?.visible()
                        viewModel.submitReport(productId.toIntOrNull() ?: 0,
                                reasonItem.categoryId, adapter.inputs, this@ProductReportSubmitFragment::onSuccessSubmit,
                                this@ProductReportSubmitFragment::onFailSubmit)
                        adapter.inputs
                    })
                    setSecondaryOnClickListner(View.OnClickListener {
                        tracking.eventReportCancelDisclaimer(reasonItem.value.toLowerCase())
                        dismiss()
                    })
                }
            }

            adapter = ReportFormAdapter(reasonItem, tracking, this::openInputDetail,
                    this::openPhotoPicker, this::onSubmitClicked)
            recycler_view.adapter = adapter
        }
    }

    private fun onSuccessSubmit(isSuccess: Boolean){
        tracking.eventReportLaporDisclaimer(adapter.trackingReasonLabel, isSuccess)
        loading_view?.gone()
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
        loading_view?.gone()
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
                val holder = recycler_view.findViewHolderForAdapterPosition(i-1)
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

    override fun onDestroy() {
        viewModel.flush()
        super.onDestroy()
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