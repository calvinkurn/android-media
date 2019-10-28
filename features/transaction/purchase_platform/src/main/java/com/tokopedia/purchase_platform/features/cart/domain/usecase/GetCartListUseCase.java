package com.tokopedia.purchase_platform.features.cart.domain.usecase;

/**
 * @author anggaprasetiyo on 25/04/18.
 */
//@Deprecated
//public class GetCartListUseCase extends UseCase<CartListData> {
//    public static final String PARAM_REQUEST_AUTH_MAP_STRING = "PARAM_REQUEST_AUTH_MAP_STRING";
//    public static final String PARAM_SELECTED_CART_ID = "selected_cart_id";
//    private final ICartRepository cartRepository;
//    private final CartMapperV3 cartMapper;
//
//    public GetCartListUseCase(ICartRepository cartRepository, CartMapperV3 cartMapper) {
//        this.cartRepository = cartRepository;
//        this.cartMapper = cartMapper;
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public Observable<CartListData> createObservable(RequestParams requestParams) {
//        TKPDMapParam<String, String> param = (TKPDMapParam<String, String>)
//                requestParams.getObject(PARAM_REQUEST_AUTH_MAP_STRING);
//        param.put(PARAM_SELECTED_CART_ID, requestParams.getString(PARAM_SELECTED_CART_ID, "0"));
//        return cartRepository.getShopGroupList(param)
//                .map(cartMapper::convertToCartItemDataList);
//    }
//
//}
