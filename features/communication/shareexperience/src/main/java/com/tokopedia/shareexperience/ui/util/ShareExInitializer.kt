package com.tokopedia.shareexperience.ui.util

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.shareexperience.data.di.DaggerShareExComponent
import com.tokopedia.shareexperience.data.util.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.request.affiliate.ShareExAffiliateEligibilityRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetAffiliateEligibilityUseCase
import com.tokopedia.shareexperience.ui.uistate.ShareExInitializationUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * This class is used to prepare the share feature
 * Put anything that share feature needed to do before showing the UI in here
 * How to use:
 * ShareExInitializer(context).run {
 *      initialize()
 *      openBottomSheet()
 * }
 */
class ShareExInitializer(context: Context) {

    private val contextRef = WeakReference(context)

    @Inject
    lateinit var useCase: ShareExGetAffiliateEligibilityUseCase

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    init {
        initInjector()
    }

    private fun initInjector() {
        val baseMainApplication = contextRef.get()?.applicationContext as? BaseMainApplication
        baseMainApplication?.let {
            DaggerShareExComponent
                .builder()
                .baseAppComponent(it.baseAppComponent)
                .build()
                .inject(this)
        }
    }

    fun initialize(
        scope: LifecycleCoroutineScope,
        affiliateEligibilityRequest: ShareExAffiliateEligibilityRequest,
        onSuccess: (ShareExInitializationUiState) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onLoading: () -> Unit = {}
    ) {
        scope.launch {
            try {
                // Use combine when we need more use case to be called
                useCase.getData(affiliateEligibilityRequest).collectLatest {
                    withContext(dispatchers.main) { // Make sure that the thread is main
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
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                onError(throwable)
            }
        }
    }

    fun openShareBottomSheet(
        id: String,
        pageSource: ShareExPageTypeEnum,
        defaultUrl: String,
        tracker: String
    ) {
        contextRef.get()?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalCommunication.SHARE_EXPERIENCE)
            intent.putExtra(ApplinkConstInternalCommunication.ID, id)
            intent.putExtra(ApplinkConstInternalCommunication.SHARE_DEFAULT_URL, defaultUrl)
            intent.putExtra(ApplinkConstInternalCommunication.SOURCE, pageSource.value)
            intent.putExtra(ApplinkConstInternalCommunication.SHARE_TRACKER, tracker)
            it.startActivity(intent)
        }
    }

    private fun isShareAffiliateIconEnabled(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.AFFILIATE_SHARE_ICON
        ) == RollenceKey.AFFILIATE_SHARE_ICON && !GlobalConfig.isSellerApp()
    }
}
