package com.tokopedia.shopadmin.feature.invitationaccepted.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase.GetArticleDetailUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase.GetPermissionListUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.AdminPermissionUiModel
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.ArticleDetailUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InvitationAcceptedViewModel @Inject constructor(
    private val getPermissionListUseCase: Lazy<GetPermissionListUseCase>,
    private val getArticleDetailUseCase: Lazy<GetArticleDetailUseCase>,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _adminPermission = MutableLiveData<Result<List<AdminPermissionUiModel>>>()
    val adminPermission: LiveData<Result<List<AdminPermissionUiModel>>>
        get() = _adminPermission

    private val _articleDetail = MutableLiveData<Result<ArticleDetailUiModel>>()
    val articleDetail: LiveData<Result<ArticleDetailUiModel>>
        get() = _articleDetail

    fun fetchAdminPermission(shopId: String) {
        launchCatchError(block = {
            val invitationAcceptedResponse = withContext(dispatchers.io) {
                getPermissionListUseCase.get().execute(shopId)
            }
            _adminPermission.value = Success(invitationAcceptedResponse)
        }, onError = {
            _adminPermission.value = Fail(it)
        })
    }

    fun fetchArticleDetail() {
        launchCatchError(block = {
            val articleDetailResponse = withContext(dispatchers.io) {
                getArticleDetailUseCase.get().execute()
            }
            _articleDetail.value = Success(articleDetailResponse)
        }, onError = {
            _articleDetail.value = Fail(it)
        })
    }
}