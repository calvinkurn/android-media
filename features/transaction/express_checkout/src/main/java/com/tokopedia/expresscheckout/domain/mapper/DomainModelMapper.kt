package com.tokopedia.expresscheckout.domain.mapper

import com.tokopedia.expresscheckout.data.entity.ExpressCheckoutResponse
import com.tokopedia.expresscheckout.domain.model.*
import com.tokopedia.transactiondata.entity.response.shippingaddressform.GroupShop

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

class DomainModelMapper : DataMapper {

    override fun convertToDomainModel(expressCheckoutResponse: ExpressCheckoutResponse): ResponseModel {

        var responseModel = ResponseModel()
        responseModel.status = expressCheckoutResponse.status

        var headerModel = HeaderModel()
        headerModel.errorCode = expressCheckoutResponse.header.errorCode
        headerModel.errors = expressCheckoutResponse.header.errors
        headerModel.processTime = expressCheckoutResponse.header.processTime
        headerModel.reason = expressCheckoutResponse.header.reason
        responseModel.headerModel = headerModel

        var dataModel = DataModel()
        dataModel.errorCode = expressCheckoutResponse.data.errorCode
        dataModel.errors = expressCheckoutResponse.data.errors
        dataModel.success = expressCheckoutResponse.data.success

//        var cartModel = CartModel()
//        cartModel.errors = expressCheckoutResponse.data.cart.errors
//        cartModel.groupShopModels
//        for (groupShop: GroupShop in expressCheckoutResponse.data.cart.groupShops) {
//            var groupShopModel = GroupShopModel()
//            groupShopModel.errors = expressCheckoutResponse.data.cart.groupShops
//            groupShopModel.
//        }
//        cartModel.groupShopModels = expressCheckoutResponse.data.cart.groupShops
//
//        dataModel.cartModel = expressCheckoutResponse.data.responseModel.dataModel = dataModel

        return responseModel
    }

}