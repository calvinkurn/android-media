package com.tokopedia.pms.proof.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.imagepicker.common.ImagePickerBuilder.Companion.getOriginalImageBuilder
import com.tokopedia.imagepicker.common.ImagePickerPageSource
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor.extract
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.imagepicker.common.putParamPageSource
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.pms.R
import com.tokopedia.pms.analytics.PmsAnalytics
import com.tokopedia.pms.analytics.PmsEvents
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.domain.data.extractValues
import com.tokopedia.pms.proof.di.DaggerUploadProofPaymentComponent
import com.tokopedia.pms.proof.di.UploadProofPaymentModule
import com.tokopedia.pms.proof.model.PaymentProofResponse
import com.tokopedia.pms.proof.view.UploadProofPaymentActivity.Companion.PAYMENT_LIST_MODEL_EXTRA
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_upload_proof_payment.*
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 7/6/18.
 */
class UploadProofPaymentFragment : BaseDaggerFragment(), UploadProofPaymentContract.View {
    @Inject
    lateinit var uploadProofPaymentPresenter: UploadProofPaymentPresenter
    @Inject
    lateinit var pmsAnalytics: dagger.Lazy<PmsAnalytics>

    private var paymentListModel: BasePaymentModel? = null
    private var progressDialog: LoaderDialog? = null
    private var imageUrl = ""
    private var isUploaded = false
    override fun getScreenName() = null

    override fun initInjector() {
        DaggerUploadProofPaymentComponent.builder()
            .uploadProofPaymentModule(UploadProofPaymentModule())
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
        uploadProofPaymentPresenter.attachView(this)
    }

    override fun onDestroy() {
        uploadProofPaymentPresenter.detachView()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            paymentListModel = it.getParcelable(PAYMENT_LIST_MODEL_EXTRA)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upload_proof_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_save.setOnClickListener(View.OnClickListener { actionButtonUpload() })
        iv_action_image.setOnClickListener(View.OnClickListener {
            if (!isUploaded) {
                resetImageUrl()
                invalidateView()
            }
        })
        button_save_choose_another_image.setOnClickListener(View.OnClickListener {
            sendUploadPmsEvents(PmsEvents.SelectAnotherImageEvent(13))
            openImagePicker()
        })
    }

    private fun resetImageUrl() {
        imageUrl = ""
    }

    private fun invalidateView() {
        if (!TextUtils.isEmpty(imageUrl)) {
            image_payment?.loadImageFitCenter(imageUrl)
            container_image_helper!!.visibility = View.VISIBLE
            container_helper!!.visibility = View.GONE
            if (isUploaded) {
                val drawable = getIconUnifyDrawable(
                    requireContext(),
                    IconUnify.CHECK_BIG,
                    ContextCompat.getColor(
                        requireContext(),
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )

                iv_action_image?.setImageDrawable(drawable)
                button_save.setText(R.string.payment_label_finish)
                button_save_choose_another_image!!.visibility = View.VISIBLE
                text_confirmation.setText(R.string.payment_label_succes_upload_proof)
            } else {
                val drawable = getIconUnifyDrawable(
                    requireContext(),
                    IconUnify.CLOSE
                )
                iv_action_image.setImageDrawable(drawable)
                button_save.setText(R.string.payment_label_save_image)
                button_save_choose_another_image.visibility = View.GONE
                text_confirmation.setText(R.string.payment_label_confirmation_upload_image)
            }
        } else {
            button_save.setText(R.string.payment_label_choose_image)
            button_save_choose_another_image.visibility = View.GONE
            container_helper.visibility = View.VISIBLE
            container_image_helper.visibility = View.GONE
        }
    }

    private fun actionButtonUpload() {
        if (button_save.text.toString() == getString(R.string.payment_label_choose_image)) {
            sendUploadPmsEvents(PmsEvents.SelectImageEvent(12))
            openImagePicker()
        } else if (button_save.text.toString() == getString(R.string.payment_label_finish)) {
            activity?.finish()
        } else {
            sendUploadPmsEvents(PmsEvents.ConfirmSelectedImageEvent(14))
            if (paymentListModel != null) {
                val (first, second) = paymentListModel!!.extractValues()
                uploadProofPaymentPresenter.uploadProofPayment(first, second, imageUrl)
            }
        }
    }

    private fun openImagePicker() {
        val builder = getOriginalImageBuilder(requireContext())
        val intent =
            RouteManager.getIntent(requireContext(), ApplinkConstInternalGlobal.IMAGE_PICKER)
        intent.putImagePickerBuilder(builder)
        intent.putParamPageSource(ImagePickerPageSource.UPLOAD_PROOF_PAYMENT_PAGE)
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PROOF)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PROOF && resultCode == Activity.RESULT_OK && data != null) {
            val imageUrlOrPathList: List<String> = extract(data).imageUrlOrPathList
            if (imageUrlOrPathList.isNotEmpty()) {
                imageUrl = imageUrlOrPathList[0]
            }
            isUploaded = false
            invalidateView()
        }
    }

    override fun showLoadingDialog() {
        context?.let {
            progressDialog = LoaderDialog(it)
            progressDialog?.dialog?.setOverlayClose(false)
            progressDialog?.show()
        }
    }

    override fun hideLoadingDialog() {
        progressDialog?.dialog?.dismiss()
        progressDialog = null
    }

    override fun onErrorUploadProof(e: Throwable) {
        showToast(
            com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context, e),
            Toaster.TYPE_ERROR
        )
    }

    override fun onResultUploadProof(paymentProofResponse: PaymentProofResponse) {
        if (paymentProofResponse.status != null
            && paymentProofResponse.status.equals("OK", ignoreCase = true)
        ) {
            isUploaded = true
            invalidateView()
            activity?.setResult(Activity.RESULT_OK)
        } else {
            if (view != null && paymentProofResponse.messageError != null)
                showToast(paymentProofResponse.messageError!!, Toaster.TYPE_ERROR)
        }
    }

    private fun showToast(message: String, toastType: Int) {
        view?.let {
            Toaster.make(it, message, Toaster.LENGTH_SHORT, toastType)
        }
    }

    private fun sendUploadPmsEvents(event: PmsEvents) {
        pmsAnalytics.get().sendPmsAnalyticsEvent(event)
    }

    companion object {
        private const val REQUEST_CODE_IMAGE_PROOF = 845
        const val MAX_FILE_SIZE_IN_KB = 10240
        fun createInstance(paymentListModel: BasePaymentModel?): Fragment {
            val uploadProofPaymentFragment = UploadProofPaymentFragment()
            val bundle = Bundle()
            bundle.putParcelable(PAYMENT_LIST_MODEL_EXTRA, paymentListModel)
            uploadProofPaymentFragment.arguments = bundle
            return uploadProofPaymentFragment
        }
    }
}