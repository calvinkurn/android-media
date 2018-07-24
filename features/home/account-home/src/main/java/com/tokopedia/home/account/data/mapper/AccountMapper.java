package com.tokopedia.home.account.data.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.AccountViewModel;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuTitleViewModel;
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

    private Context context;

    @Inject
    AccountMapper(@ApplicationContext Context context) {
        this.context = context;
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

        MenuTitleViewModel menuTitle = new MenuTitleViewModel();
        menuTitle.setTitle(context.getString(R.string.title_menu_transaction));
        visitables.add(menuTitle);

        MenuListViewModel menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_waiting_for_payment));
        menuList.setMenuDescription(context.getString(R.string.label_menu_waiting_for_payment));
        menuList.setApplink("tokopedia://dev-option");
        visitables.add(menuList);

        List<MenuGridItemViewModel> menuGridItems = new ArrayList<>();
        MenuGridItemViewModel gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokopoint,
                context.getString(R.string.label_menu_waiting_confirmation),
                "tokopedia://home",
                3
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.label_menu_order_processed),
                "tokopedia://buyer/payment",
                2
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.label_menu_shipping),
                "tokopedia://buyer/payment",
                1
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.label_menu_delivered),
                "tokopedia://buyer/payment",
                0
        );
        menuGridItems.add(gridItem);

        MenuGridViewModel menuGrid = new MenuGridViewModel();
        menuGrid.setTitle(context.getString(R.string.title_menu_shopping_transaction));
        menuGrid.setLinkText(context.getString(R.string.label_menu_show_tx_history));
        menuGrid.setApplinkUrl("tokopedia://buyer");
        menuGrid.setItems(menuGridItems);
        visitables.add(menuGrid);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_complain));
        menuList.setMenuDescription(context.getString(R.string.label_menu_complain));
        menuList.setApplink("tokopedia://dev-option");
        visitables.add(menuList);

        menuGrid = new MenuGridViewModel();
        menuGrid.setTitle(context.getString(R.string.title_menu_other_transaction));
        menuGridItems = new ArrayList<>();
        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.title_menu_top_up_bill),
                "tokopedia://dev-option",
                0
        );
        menuGridItems.add(gridItem);
        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.title_menu_deals),
                "tokopedia://dev-option",
                0
        );
        menuGridItems.add(gridItem);
        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.title_menu_flight),
                "tokopedia://dev-option",
                0
        );
        menuGridItems.add(gridItem);
        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.title_menu_show_all),
                "tokopedia://dev-option",
                0
        );
        menuGridItems.add(gridItem);
        menuGrid.setItems(menuGridItems);
        visitables.add(menuGrid);

        menuTitle = new MenuTitleViewModel();
        menuTitle.setTitle(context.getString(R.string.title_menu_favorites));
        visitables.add(menuTitle);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_last_seen));
        menuList.setMenuDescription(context.getString(R.string.label_menu_last_seen));
        menuList.setApplink("tokopedia://dev-option");
        visitables.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_favorite_shops));
        menuList.setMenuDescription(context.getString(R.string.label_menu_favorite_shops));
        menuList.setApplink("tokopedia://dev-option");
        visitables.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_top_up_bill_subscription));
        menuList.setMenuDescription(context.getString(R.string.label_menu_top_up_bill_subscription));
        menuList.setApplink("tokopedia://dev-option");
        visitables.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_top_up_numbers));
        menuList.setMenuDescription(context.getString(R.string.label_menu_top_up_numbers));
        menuList.setApplink("tokopedia://dev-option");
        visitables.add(menuList);

        InfoCardViewModel infoCard = new InfoCardViewModel();
        infoCard.setMainText(context.getString(R.string.title_menu_wallet_referral));
        infoCard.setSecondaryText(context.getString(R.string.label_menu_wallet_referral));
        visitables.add(infoCard);

        menuTitle = new MenuTitleViewModel();
        menuTitle.setTitle(context.getString(R.string.title_menu_help));
        visitables.add(menuTitle);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_resolution_center));
        menuList.setMenuDescription(context.getString(R.string.label_menu_resolution_center));
        menuList.setApplink("tokopedia://dev-option");
        visitables.add(menuList);

        return visitables;
    }

    private List<Visitable> getSellerModel(AccountModel accountModel) {
        return new ArrayList<>();
    }
}
