package com.tokopedia.shareexperience.ui.util

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.data.di.DaggerShareExComponent
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.request.affiliate.ShareExAffiliateEligibilityRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetAffiliateEligibilityUseCase
import com.tokopedia.shareexperience.ui.ShareExBottomSheet
import com.tokopedia.shareexperience.ui.listener.ShareExBottomSheetListener
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExInitializerArg
import com.tokopedia.shareexperience.ui.uistate.ShareExInitializationUiState
import com.tokopedia.shareexperience.ui.view.ShareExLoadingDialog
import com.tokopedia.unifycomponents.Toaster
import io.hansel.pebbletracesdk.presets.UIPresets.findViewById
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * This class is used to prepare the share feature
 * Put anything that share feature needed to do before showing the UI in here
 */
class ShareExInitializer(
    activity: FragmentActivity
): DefaultLifecycleObserver, ShareExBottomSheetListener {

    private val weakActivity = WeakReference(activity)
    private var dialog: ShareExLoadingDialog? = null
    private var bottomSheet: ShareExBottomSheet? = null
    private lateinit var bottomSheetArg: ShareExBottomSheetArg

    init {
        addObserver(activity)
        initInjector()
    }

    @Inject
    lateinit var useCase: ShareExGetAffiliateEligibilityUseCase

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    private fun addObserver(activity: FragmentActivity) {
        activity.lifecycle.addObserver(this)
    }

    private fun initInjector() {
        val baseMainApplication = weakActivity.get()?.applicationContext as? BaseMainApplication
        baseMainApplication?.let {
            DaggerShareExComponent
                .builder()
                .baseAppComponent(it.baseAppComponent)
                .build()
                .inject(this)
        }
    }

    fun start(arg: ShareExInitializerArg) {
        weakActivity.get()?.lifecycleScope?.launch {
            try {
                /**
                 * Add more checker and args here if needed
                 */
                when {
                    (arg.affiliateEligibilityRequest != null) -> {
                        observeAffiliateEligibility(
                            arg.affiliateEligibilityRequest,
                            arg.onSuccess,
                            arg.onError,
                            arg.onLoading
                        )
                    }
                    else -> Unit
                }
            } catch (throwable: Throwable) {
                arg.onError(throwable)
            }
        }

    }

    private suspend fun observeAffiliateEligibility(
        affiliateEligibilityRequest: ShareExAffiliateEligibilityRequest,
        onSuccess: (ShareExInitializationUiState) -> Unit,
        onError: (Throwable) -> Unit,
        onLoading: () -> Unit
    ) {
        useCase.getData(affiliateEligibilityRequest).collectLatest {
            when (it) {
                is ShareExResult.Success -> {
                    onSuccess(
                        ShareExInitializationUiState(
                            isEligibleAffiliate = it.data.isEligible && isShareAffiliateIconEnabled()
                        )
                    )
                }
                is ShareExResult.Error -> onError(it.throwable)
                ShareExResult.Loading -> onLoading()
            }
        }
    }

    fun openShareBottomSheet(
        bottomSheetArg: ShareExBottomSheetArg
//        trackerProvider: ShareExTrackerProvider
    ) {
        this.bottomSheetArg = bottomSheetArg
        showLoadingDialog()
    }

    private fun showLoadingDialog() {
        weakActivity.get()?.let { activity ->
            dialog = ShareExLoadingDialog(
                context = activity,
                id = bottomSheetArg.identifier,
                pageTypeEnum = bottomSheetArg.pageTypeEnum,
                onResult = ::onResultShareBottomSheetModel
            ).also { dialog ->
                dialog.show()
            }
        }
    }

    private fun onResultShareBottomSheetModel(result: ShareExResult<ShareExBottomSheetModel>) {
        dialog?.dismiss()
        weakActivity.get()?.let {
            when (result) {
                is ShareExResult.Success -> {
                    bottomSheetArg = bottomSheetArg.copy(
                        bottomSheetModel = result.data
                    )
                    showShareBottomSheet(it)
                }
                is ShareExResult.Error -> {
                    bottomSheetArg = bottomSheetArg.copy(
                        throwable = result.throwable
                    )
                    showShareBottomSheet(it)
                }
                ShareExResult.Loading -> Unit
            }
        }
    }

    private fun showShareBottomSheet(fragmentActivity: FragmentActivity) {
        bottomSheet = ShareExBottomSheet.newInstance(bottomSheetArg)
        bottomSheet?.setListener(this)
        bottomSheet?.show(fragmentActivity.supportFragmentManager, "")
    }

    override fun onSuccessCopyLink() {
        weakActivity.get()?.let {
            val contentView: View? = it.findViewById(android.R.id.content)
            contentView?.let { view ->
                Toaster.build(
                    view = view,
                    text = it.getString(R.string.shareex_success_copy_link),
                    duration = Toaster.LENGTH_SHORT,
                    type = Toaster.TYPE_NORMAL
                ).show()
            }
        }
    }

    override fun refreshPage() {
        bottomSheet?.dismiss()
        showLoadingDialog()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        dialog?.dismiss()
        dialog = null
        bottomSheet = null
    }

    private fun isShareAffiliateIconEnabled(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.AFFILIATE_SHARE_ICON
        ) == RollenceKey.AFFILIATE_SHARE_ICON && !GlobalConfig.isSellerApp()
    }
}
