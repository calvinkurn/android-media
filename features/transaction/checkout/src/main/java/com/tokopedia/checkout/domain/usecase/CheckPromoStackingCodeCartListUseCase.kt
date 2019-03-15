package com.tokopedia.checkout.domain.usecase

import com.tokopedia.promocheckout.common.di.PromoCheckoutQualifier
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.mapper.ICheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.transactiondata.repository.ICartRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by fwidjaja on 14/03/19.
 */
class CheckPromoStackingCodeCartListUseCase @Inject
constructor(private val checkPromoStackingCodeMapper: ICheckPromoStackingCodeMapper,
            @param:PromoCheckoutQualifier private val checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase) : UseCase<DataUiModel>() {


    override fun createObservable(requestParams: RequestParams): Observable<DataUiModel>? {
        // val paramCheckPromo = requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO) as Map<String, String>
        val paramUpdateCart = requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART) as Map<String, String>

        /*override fun getProductData(device: String, ob: Int, q: String?, rows: Int, source: String, start: Int): Single<List<ProductsItemUiModel>> {
            return apiService.searchProduct(device, ob, q, rows, source, start)
                    .map { ProductMapper.responseToListUiModel(it) }
        }*/

        /*if (paramUpdateCart != null)
            cartRepository.updateCartData(paramUpdateCart)
                    .flatMap { updateCartDataResponse ->
                        checkPromoStackingCodeUseCase.createObservable(checkPromoStackingCodeUseCase.createRequestParams())
                                .map<PromoCodeCartListData>(
                                        Func1<DataVoucher, PromoCodeCartListData> { voucherCouponMapper.convertPromoCodeCartListData(it) }
                                )
                    }
        else*/
            return checkPromoStackingCodeUseCase.createObservable(checkPromoStackingCodeUseCase.createRequestParams())
                    .map<DataUiModel> { checkPromoStackingCodeMapper.convertResponseDataModel(it) }
    }

    companion object {
        val PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO = "PARAM_REQUEST_AUTH_MAP_STRING_CHECK_PROMO"
        val PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART = "PARAM_REQUEST_AUTH_MAP_STRING_UPDATE_CART"

        val PARAM_PROMO_LANG = "lang"
        val PARAM_PROMO_CODE = "promo_code"
        val PARAM_PROMO_SUGGESTED = "suggested"
        val PARAM_VALUE_SUGGESTED = "1"
        val PARAM_VALUE_NOT_SUGGESTED = "0"
        val PARAM_VALUE_LANG_ID = "id"
        val PARAM_CARTS = "carts"
        val PARAM_ONE_CLICK_SHIPMENT = "is_one_click_shipment"
    }
}