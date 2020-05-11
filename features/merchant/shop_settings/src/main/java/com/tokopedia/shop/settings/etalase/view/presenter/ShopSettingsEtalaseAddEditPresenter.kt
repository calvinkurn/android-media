package com.tokopedia.shop.settings.etalase.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.AddShopEtalaseUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.UpdateShopEtalaseUseCase
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseAddEditFragment
import com.tokopedia.shop.settings.etalase.view.listener.ShopSettingsEtalaseAddEditView
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class ShopSettingsEtalaseAddEditPresenter @Inject constructor(private val addShopEtalaseUseCase: AddShopEtalaseUseCase,
                                                              private val updateShopEtalaseUseCase: UpdateShopEtalaseUseCase,
                                                              private val getShopEtalase: GetShopEtalaseUseCase,
                                                              private val userSession: UserSessionInterface)
    : BaseDaggerPresenter<ShopSettingsEtalaseAddEditView>() {

    var etalaseCount = 0
    var listEtalaseModel: ArrayList<ShopEtalaseModel>? = null

    override fun detachView() {
        super.detachView()
        addShopEtalaseUseCase.unsubscribe()
        updateShopEtalaseUseCase.unsubscribe()
    }

    fun saveEtalase(etalaseModel: ShopEtalaseViewModel, isEdit: Boolean = false) {
        val useCase: UseCase<String> = if (!isEdit) addShopEtalaseUseCase else updateShopEtalaseUseCase
        val requestParams = AddShopEtalaseUseCase.createRequestParams(etalaseModel.name)

        if (isEdit) {
            requestParams.putString(ID, etalaseModel.id)
        }

        useCase.execute(requestParams, object : Subscriber<String>() {
            override fun onNext(string: String?) {
                view?.onSuccesAddEdit(string)
            }

            override fun onCompleted() {}

            override fun onError(throwable: Throwable?) {
                view?.onErrorAddEdit(throwable)
            }
        })
    }

    fun getEtalaseList() {
        view?.showLoading()
        getShopEtalase.execute(
                GetShopEtalaseUseCase.createRequestParams(true),
                object : Subscriber<ArrayList<ShopEtalaseModel>>() {
                    override fun onNext(listEtalase: ArrayList<ShopEtalaseModel>?) {
                        view?.hideLoading()
                        listEtalase?.let {
                            listEtalaseModel = it
                            etalaseCount = listEtalase.filter { it.type!= DEFAULT_ETALASE_TYPE }.size
                            view?.onSuccessGetEtalaseList()
                        }
                    }

                    override fun onCompleted() {}

                    override fun onError(error: Throwable?) {
                        view?.hideLoading()
                        view?.onErrorGetEtalaseList(error)
                    }

                })
    }

    fun isIdlePowerMerchant() = userSession.isPowerMerchantIdle

    fun isPowerMerchant() = userSession.isGoldMerchant

    fun isEtalaseCountAtMax() = etalaseCount >= ShopSettingsEtalaseAddEditFragment.MAXIMUN_ETALASE_COUNT

    fun isEtalaseDuplicate(query: String): Boolean {
        return listEtalaseModel?.any { it.name == query } ?: false
    }

    companion object {
        private const val ID = "id"
        private const val DEFAULT_ETALASE_TYPE  = -1
    }
}