package com.tokopedia.digital.wallets;

import com.tokopedia.digital.R;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public class WalletToDepositPresenter implements IWalletToDepositPresenter {

    private final IWalletToDepositView view;
    private final Object interactor; //TODO bikin interactor

    public WalletToDepositPresenter(IWalletToDepositView view, Object interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    //TODO ubah nama methodnya
    @Override
    public void getSomethings() {
        //TODO kalau perlu lakuin hit api untuk dapet content data, lakuin lewat interactor

        String titlePage = view.getTitlePageFromPassData();
        int amount = view.getWalletAmountFromPassData();
        String amountFormatted = view.getWalletAmountFormattedFromPassData();

        view.showInitialProgressLoading();
        view.renderSomethingFromGet(
                new WalletToDepositData.Builder()
                        .subTitle("Anda akan memindahkan dana dari Tokocash ke Saldo Tokopedia sebesar")
                        .title(titlePage)
                        .nominalFormatted(amountFormatted)
                        .titleButtonNegative("Batal")
                        .titleButtonPositive("Pindahkan ke Saldo")
                        .build()
        );
        view.hideInitialProgressLoading();
    }

    //TODO ubah nama methodnya
    @Override
    public void processSomethings() {
        //TODO lakuin interactor, dibawah cuma dummy data model ui

        WalletToDepositThanksData dummyWalletToDepositThanksDataSuccess =
                new WalletToDepositThanksData.Builder()
                        .title("Dana Telah Dipindahkan")
                        .subTitle("Pemindahan Dana ke Saldo Tokopedia berhasil")
                        .description("Anda berhasil memindahkan Rp.150.000 ke Saldo Tokopedia")
                        .titleButtonPositive("Ke Halaman Tokocash")
                        .typeResult(WalletToDepositThanksData.TypeResult.SUCCESS)
                        .iconResId(R.drawable.ic_wallet_to_deposit_thanks_success)
                        .build();

        WalletToDepositThanksData dummyWalletToDepositThanksDataFailed =
                new WalletToDepositThanksData.Builder()
                        .title("Dana Gagal Dipindahkan")
                        .subTitle("Pemindahan Dana ke Saldo Tokopedia Gagal")
                        .titleButtonNegative("Ke Halaman Tokocash")
                        .titleButtonPositive("Coba Lagi")
                        .typeResult(WalletToDepositThanksData.TypeResult.FAILED)
                        .iconResId(R.drawable.ic_wallet_to_deposit_thanks_success)
                        .build();


        view.renderSomethingFromProcess(dummyWalletToDepositThanksDataSuccess);
    }
}
