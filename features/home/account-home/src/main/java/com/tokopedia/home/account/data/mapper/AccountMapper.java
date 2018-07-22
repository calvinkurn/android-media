package com.tokopedia.home.account.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.presentation.viewmodel.AccountViewModel;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountMapper implements Func1<GraphqlResponse, AccountViewModel> {

    public static final String LABEL_TOKO_CASH = "TokoCash";
    public static final String LABEL_DEPOSIT = "Saldo";

    @Inject
    AccountMapper() {
    }

    @Override
    public AccountViewModel call(GraphqlResponse graphqlResponse) {
        AccountModel accountModel = graphqlResponse.getData(AccountModel.class);

        AccountViewModel accountViewModel = new AccountViewModel();
        accountViewModel.setBuyerViewModels(getBuyerModel(accountModel));
        if (accountModel.getShopInfo() != null
                && accountModel.getShopInfo().getInfo() != null
                && !TextUtils.isEmpty(accountModel.getShopInfo().getInfo().getShopId())) {
            accountViewModel.setSellerViewModels(getSellerModel(accountModel));
            accountViewModel.setSeller(true);
        } else {
            accountViewModel.setSeller(false);
        }
        return accountViewModel;
    }

    private List<Visitable> getBuyerModel(AccountModel accountModel) {
        List<Visitable> visitables = new ArrayList<>();

        BuyerCardViewModel buyerCardViewModel = new BuyerCardViewModel();
        buyerCardViewModel.setName(accountModel.getProfile().getFullName());
        buyerCardViewModel.setTokopoint(accountModel.getTokopoints().getStatus().getPoints().getRewardStr());
        buyerCardViewModel.setImageUrl(accountModel.getProfile().getProfilePicture());
        buyerCardViewModel.setProgress(accountModel.getProfile().getCompletion());
        visitables.add(buyerCardViewModel);

        TokopediaPayViewModel tokopediaPayViewModel = new TokopediaPayViewModel();
        tokopediaPayViewModel.setLabelLeft(LABEL_TOKO_CASH);
        tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getBalance());
        tokopediaPayViewModel.setLabelRight(LABEL_DEPOSIT);
        tokopediaPayViewModel.setAmountRight(accountModel.getDeposit().getDepositFmt());
        visitables.add(tokopediaPayViewModel);

        return visitables;
    }

    private List<Visitable> getSellerModel(AccountModel accountModel) {
        return new ArrayList<>();
    }
}
