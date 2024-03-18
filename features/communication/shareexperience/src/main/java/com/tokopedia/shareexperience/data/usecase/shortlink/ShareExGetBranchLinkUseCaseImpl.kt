package com.tokopedia.shareexperience.data.usecase.shortlink

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.repository.ShareExBranchRepository
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetBranchLinkUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
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
            branchUniversalObject.generateShortUrl(
                context,
                linkProperties,
                branchRepository.getBranchListener(this)
            )
            awaitClose { channel.close() }
        }
            .catch {
                emit(ShareExResult.Error(it))
            }
            .flowOn(dispatchers.io)
    }
}
