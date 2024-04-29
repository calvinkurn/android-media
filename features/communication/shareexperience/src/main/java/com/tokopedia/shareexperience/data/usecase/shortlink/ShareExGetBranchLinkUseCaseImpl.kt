package com.tokopedia.shareexperience.data.usecase.shortlink

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.repository.ShareExBranchRepository
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetBranchLinkUseCase
import com.tokopedia.shareexperience.domain.util.ShareExConstants.ShortLinkValue.TIMEOUT_LIMIT
import com.tokopedia.shareexperience.domain.util.ShareExResult
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.util.LinkProperties
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import javax.inject.Inject

class ShareExGetBranchLinkUseCaseImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val branchRepository: ShareExBranchRepository,
    private val dispatchers: CoroutineDispatchers
) : ShareExGetBranchLinkUseCase {

    override suspend fun getLink(
        params: ShareExBranchLinkPropertiesRequest
    ): Flow<ShareExResult<String>> {
        return callbackFlow {
            val branchUniversalObject = branchRepository.generateBranchUniversalObject(
                params.branchUniversalObjectRequest
            )
            val linkProperties = branchRepository.generateLinkProperties(params)
            val job = launch {
                generateShortLink(this@callbackFlow, branchUniversalObject, linkProperties)
            }
            awaitClose {
                if (job.isActive) {
                    job.cancel()
                }
                channel.close()
            }
        }
            .catch {
                emit(ShareExResult.Error(it))
            }
            .flowOn(dispatchers.io)
    }

    private suspend fun generateShortLink(
        scope: ProducerScope<ShareExResult<String>>,
        branchUniversalObject: BranchUniversalObject,
        linkProperties: LinkProperties,
    ) {
        try {
            withTimeout(getTimeoutLimit()) {
                branchUniversalObject.generateShortUrl(
                    context,
                    linkProperties,
                    branchRepository.getBranchListener(scope)
                )
            }
        } catch (e: TimeoutCancellationException) {
            Timber.d(e)
            scope.send(ShareExResult.Error(e))
            scope.close()
        }
    }

    private fun getTimeoutLimit(): Long {
        return TIMEOUT_LIMIT
    }
}
