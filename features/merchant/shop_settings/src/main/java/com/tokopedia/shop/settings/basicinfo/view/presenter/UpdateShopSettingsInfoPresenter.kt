package com.tokopedia.shop.settings.basicinfo.view.presenter

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopBasicDataUseCase
import com.tokopedia.shop.settings.basicinfo.data.UploadShopEditImageModel
import com.tokopedia.shop.settings.basicinfo.domain.UploadShopImageUseCase
import com.tokopedia.usecase.RequestParams

import javax.inject.Inject

import rx.Subscriber


class UpdateShopSettingsInfoPresenter @Inject
constructor(private val getShopBasicDataUseCase: GetShopBasicDataUseCase,
            private val updateShopBasicDataUseCase: UpdateShopBasicDataUseCase,
            private val uploadShopImageUseCase: UploadShopImageUseCase) : BaseDaggerPresenter<UpdateShopSettingsInfoPresenter.View>() {

    interface View : CustomerView {
        fun onSuccessUpdateShopBasicData(successMessage: String)
        fun onErrorUpdateShopBasicData(throwable: Throwable)
        fun onSuccessGetShopBasicData(shopBasicDataModel: ShopBasicDataModel)
        fun onErrorGetShopBasicData(throwable: Throwable)
        fun onErrorUploadShopImage(throwable: Throwable)
    }

    fun getShopBasicData() {
        getShopBasicDataUseCase.unsubscribe()
        getShopBasicDataUseCase.execute(RequestParams.EMPTY, object : Subscriber<ShopBasicDataModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.onErrorGetShopBasicData(e)
                }
            }

            override fun onNext(shopBasicDataModel: ShopBasicDataModel) {
                if (isViewAttached) {
                    view.onSuccessGetShopBasicData(shopBasicDataModel)
                }
            }
        })
    }

    fun uploadShopImage(imagePath: String, tagline: String, description: String) {
        uploadShopImageUseCase.unsubscribe()
        uploadShopImageUseCase.execute(UploadShopImageUseCase.createRequestParams(imagePath),
                object : Subscriber<UploadShopEditImageModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        view?.onErrorUploadShopImage(e)
                    }

                    override fun onNext(uploadShopEditImageModel: UploadShopEditImageModel) {
                        updateShopBasicData(tagline, description, uploadShopEditImageModel.data!!.image!!.picCode)
                    }
                })
    }

    fun updateShopBasicData(tagline: String, description: String) {
        updateShopBasicDataUseCase.unsubscribe()
        updateShopBasicDataUseCase.execute(UpdateShopBasicDataUseCase.createRequestParams(tagline,
                description, null, null, null), createUpdateBasicInfoSubscriber())
    }

    private fun updateShopBasicData(tagline: String, description: String, logoCode: String?) {
        updateShopBasicDataUseCase.unsubscribe()
        updateShopBasicDataUseCase.execute(UpdateShopBasicDataUseCase.createRequestParams(tagline,
                description, logoCode, null, null), createUpdateBasicInfoSubscriber())
    }

    private fun createUpdateBasicInfoSubscriber(): Subscriber<String> {
        return object : Subscriber<String>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view?.onErrorUpdateShopBasicData(e)
            }

            override fun onNext(successMessage: String) {
                view?.onSuccessUpdateShopBasicData(successMessage)
            }
        }
    }

    override fun detachView() {
        super.detachView()
        getShopBasicDataUseCase.unsubscribe()
        updateShopBasicDataUseCase.unsubscribe()
        uploadShopImageUseCase.unsubscribe()
    }


}
