package com.tokopedia.shareexperience.ui.view

import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.data.di.component.ShareExComponentFactoryProvider
import com.tokopedia.shareexperience.databinding.ShareexperienceLoadingOverlayBinding
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExDefaultBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExDiscoveryBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExLiveBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExProductBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExReviewBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExShopBottomSheetRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.domain.util.ShareExLogger
import com.tokopedia.shareexperience.domain.util.ShareExResult
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

@SuppressLint("UnifyComponentUsage")
class ShareExLoadingDialog(
    context: Context,
    private val arg: ShareExBottomSheetArg,
    private val onResult: (ShareExResult<ShareExBottomSheetModel>) -> Unit
) : Dialog(context, R.style.CustomDialogTheme) {

    private val weakContext = WeakReference(context)
    private var _binding: ShareexperienceLoadingOverlayBinding? = null
    private val gson by lazy {
        Gson()
    }

    @Inject
    lateinit var useCase: ShareExGetSharePropertiesUseCase

    init {
        initInjector()
        setBinding()
        setListener()
        fetchBottomSheetData()
    }

    private fun initInjector() {
        val application = weakContext.get()?.applicationContext as? Application
        application?.let {
            ShareExComponentFactoryProvider
                .instance
                .createShareExComponent(it)
                .inject(this)
        }
    }

    private fun setBinding() {
        _binding = ShareexperienceLoadingOverlayBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

    private fun setListener() {
        _binding?.root?.setOnClickListener {
            this.dismiss()
        }
    }

    private fun fetchBottomSheetData() {
        weakContext.get()?.let {
            (it as? FragmentActivity)?.lifecycleScope?.launch {
                try {
                    val request = getRequest()
                    useCase.getData(request).collectLatest { result ->
                        when (result) {
                            is ShareExResult.Success, is ShareExResult.Error -> {
                                onResult(result)
                            }
                            ShareExResult.Loading -> Unit
                        }
                    }
                } catch (throwable: Throwable) {
                    Timber.d(throwable)
                    weakContext.get()?.let { context ->
                        ShareExLogger.logExceptionToServerLogger(
                            throwable = throwable,
                            deviceId = UserSession(context).deviceId,
                            description = ::fetchBottomSheetData.name
                        )
                    }
                    onResult(ShareExResult.Error(throwable))
                }
            }
        }
    }

    private fun getRequest(): ShareExBottomSheetRequest {
        return when (arg.pageTypeEnum) {
            ShareExPageTypeEnum.PDP, ShareExPageTypeEnum.THANK_YOU_PRODUCT -> {
                ShareExProductBottomSheetRequest(
                    pageType = arg.pageTypeEnum.valueInt,
                    productId = arg.productId
                )
            }
            ShareExPageTypeEnum.SHOP -> {
                ShareExShopBottomSheetRequest(
                    pageType = arg.pageTypeEnum.valueInt,
                    shopId = arg.shopId
                )
            }
            ShareExPageTypeEnum.DISCOVERY -> {
                ShareExDiscoveryBottomSheetRequest(
                    pageType = arg.pageTypeEnum.valueInt,
                    campaignId = arg.campaignId
                )
            }
            ShareExPageTypeEnum.REVIEW -> {
                ShareExReviewBottomSheetRequest(
                    pageType = arg.pageTypeEnum.valueInt,
                    reviewId = arg.reviewId,
                    attachmentId = arg.attachmentId
                )
            }
            ShareExPageTypeEnum.ORDER_DETAIL -> {
                ShareExProductBottomSheetRequest(
                    pageType = arg.pageTypeEnum.valueInt,
                    productId = arg.productId
                )
            }
            ShareExPageTypeEnum.PLAY -> {
                ShareExLiveBottomSheetRequest(
                    pageType = arg.pageTypeEnum.valueInt,
                    contentId = arg.contentId,
                    origin = arg.origin
                )
            }
            else -> {
                // Default params, if all BU specific data only need to be passed through metadata
                ShareExDefaultBottomSheetRequest(
                    pageType = arg.pageTypeEnum.valueInt,
                    metadata = gson.toJson(arg.metadata)
                )
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}
