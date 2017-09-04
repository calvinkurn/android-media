package com.tokopedia.digital.wallets.accountsetting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public class WalletAccountSettingPresenter implements IWalletAccountSettingPresenter {

    private final IWalletAccountSettingView view;
    private final Object interactor; //TODO bikin interactor


    public WalletAccountSettingPresenter(IWalletAccountSettingView view, Object interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void processGetWalletAccountData() {
        List<WalletAccountSettingConnectedUserData> dummyList = new ArrayList<>();
        dummyList.add(new WalletAccountSettingConnectedUserData.Builder()
                .email("user1@gmail.com")
                .userName("User1")
                .registeredDate("Terdaftar : Dari dahulu kala sebelum")
                .build()
        );

        dummyList.add(new WalletAccountSettingConnectedUserData.Builder()
                .email("user2@gmail.com")
                .userName("User2")
                .registeredDate("Terdaftar  Baru aja kemaren coy")
                .build()
        );

        dummyList.add(new WalletAccountSettingConnectedUserData.Builder()
                .email("user3@gmail.com")
                .userName("User3")
                .registeredDate("Terdaftar  Baru aja kemaren coy")
                .build()
        );

        dummyList.add(new WalletAccountSettingConnectedUserData.Builder()
                .email("user4@gmail.com")
                .userName("User4")
                .registeredDate("Terdaftar  Baru aja kemaren coy")
                .build()
        );

        WalletAccountSettingData dummyData = new WalletAccountSettingData();
        dummyData.setEmail("angga.keren@sukasuka.com");
        dummyData.setPhoneNumber("08966666666");
        dummyData.setSubTitle("Anda dapat mengelola TokoCash dengan akun Tokopedia melalui halaman ini.");
        dummyData.setName("Angga Keren");
        dummyData.setConnectedUserDataList(dummyList);

        view.renderWalletAccountSettingData(dummyData);
    }

    @Override
    public void processDeleteConnectedUser() {

    }
}
