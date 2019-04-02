package com.tokopedia.gm.subscribe.domain.cart.interactor;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.subscribe.domain.cart.GmSubscribeCartRepository;
import com.tokopedia.gm.subscribe.domain.cart.exception.GmVoucherCheckException;
import com.tokopedia.gm.subscribe.domain.cart.model.GmVoucherCheckDomainModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 2/3/17.
 */
public class CheckGmSubscribeVoucherUseCase extends UseCase<GmVoucherCheckDomainModel> {
    public static final String SELECTED_PRODUCT = "SELECTED_PRODUCT";
    public static final int UNDEFINED_SELECTED = -1;
    public static final String VOUCHER_CODE = "VOUCHER_CODE";
    public static final String EMPTY_VOUCHER = "";
    private final GmSubscribeCartRepository gmSubscribeCartRepository;

    @Inject
    public CheckGmSubscribeVoucherUseCase(GmSubscribeCartRepository gmSubscribeCartRepository) {
        super();
        this.gmSubscribeCartRepository = gmSubscribeCartRepository;
    }

    public static RequestParams createRequestParams(int productId, String voucherCode) {
        RequestParams params = RequestParams.create();
        params.putInt(SELECTED_PRODUCT, productId);
        params.putString(VOUCHER_CODE, voucherCode);
        return params;
    }

    @Override
    public Observable<GmVoucherCheckDomainModel> createObservable(RequestParams requestParams) {
        int selectedProduct = requestParams.getInt(SELECTED_PRODUCT, UNDEFINED_SELECTED);
        String voucherCode = requestParams.getString(VOUCHER_CODE, EMPTY_VOUCHER);
        return Observable.just(true)
                .map(new CheckInput(selectedProduct, voucherCode))
                .flatMap(new CheckVoucher(gmSubscribeCartRepository, selectedProduct, voucherCode));
    }

    private static class CheckInput implements Func1<Boolean, Boolean> {
        private final int selectedProduct;
        private final String voucherCode;

        public CheckInput(int selectedProduct, String voucherCode) {
            this.selectedProduct = selectedProduct;
            this.voucherCode = voucherCode;
        }

        @Override
        public Boolean call(Boolean aBoolean) {
            if (selectedProduct == UNDEFINED_SELECTED){
                throw new GmVoucherCheckException("Produk tidak boleh kosong");
            } else if (voucherCode.equals(EMPTY_VOUCHER)){
                throw new GmVoucherCheckException("Kode voucher harus diisi");
            }
            return true;
        }
    }

    private static class CheckVoucher implements Func1<Boolean, Observable<GmVoucherCheckDomainModel>> {
        private final GmSubscribeCartRepository gmSubscribeCartRepository;
        private final int selectedProduct;
        private final String voucherCode;

        public CheckVoucher(GmSubscribeCartRepository gmSubscribeCartRepository, int selectedProduct1, String voucherCode1) {
            this.gmSubscribeCartRepository = gmSubscribeCartRepository;
            selectedProduct = selectedProduct1;
            voucherCode = voucherCode1;
        }

        @Override
        public Observable<GmVoucherCheckDomainModel> call(Boolean aBoolean) {
            return gmSubscribeCartRepository.checkVoucher(
                    selectedProduct,
                    voucherCode
            );
        }
    }
}
