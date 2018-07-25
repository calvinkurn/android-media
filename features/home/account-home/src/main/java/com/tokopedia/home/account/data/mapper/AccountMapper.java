package com.tokopedia.home.account.data.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.AccountViewModel;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuTitleViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;

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
        accountViewModel.setBuyerViewModel(getBuyerModel(accountModel));
        if (accountModel.getShopInfo() != null
                && accountModel.getShopInfo().getInfo() != null
                && !TextUtils.isEmpty(accountModel.getShopInfo().getInfo().getShopId())) {
            accountViewModel.setSellerViewModel(getSellerModel(accountModel));
            accountViewModel.setSeller(true);
        } else {
            accountViewModel.setSeller(false);
        }
        return accountViewModel;
    }

    private BuyerViewModel getBuyerModel(AccountModel accountModel) {
        List<ParcelableViewModel> items = new ArrayList<>();

        BuyerCardViewModel buyerCardViewModel = new BuyerCardViewModel();
        buyerCardViewModel.setName(accountModel.getProfile().getFullName());
        buyerCardViewModel.setTokopoint(accountModel.getTokopoints().getStatus().getPoints().getRewardStr());
        buyerCardViewModel.setImageUrl(accountModel.getProfile().getProfilePicture());
        buyerCardViewModel.setProgress(accountModel.getProfile().getCompletion());
        items.add(buyerCardViewModel);

        TokopediaPayViewModel tokopediaPayViewModel = new TokopediaPayViewModel();
        tokopediaPayViewModel.setLabelLeft(context.getString(R.string.label_tokopedia_pay_wallet));
        tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getBalance());
        tokopediaPayViewModel.setLabelRight(context.getString(R.string.label_tokopedia_pay_deposit));
        tokopediaPayViewModel.setAmountRight(accountModel.getDeposit().getDepositFmt());
        items.add(tokopediaPayViewModel);

        MenuTitleViewModel menuTitle = new MenuTitleViewModel();
        menuTitle.setTitle(context.getString(R.string.title_menu_transaction));
        items.add(menuTitle);

        MenuListViewModel menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_waiting_for_payment));
        menuList.setMenuDescription(context.getString(R.string.label_menu_waiting_for_payment));
        // TODO: 7/25/18 need applink to PMS
        menuList.setApplink("tokopedia://dev-buyer/payment");
        items.add(menuList);

        List<MenuGridItemViewModel> menuGridItems = new ArrayList<>();
        MenuGridItemViewModel gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokopoint,
                context.getString(R.string.label_menu_waiting_confirmation),
                // TODO: 7/25/18 need applink
                "tokopedia://home",
                // TODO: 7/25/18 need notification counter
                0
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.label_menu_order_processed),
                // TODO: 7/25/18 need applink
                "tokopedia://buyer/payment",
                // TODO: 7/25/18 need notification counter
                0
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.label_menu_shipping),
                // TODO: 7/25/18 need applink
                "tokopedia://buyer/payment",
                // TODO: 7/25/18 need notification counter
                0
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.label_menu_delivered),
                // TODO: 7/25/18 need applink
                "tokopedia://buyer/payment",
                // TODO: 7/25/18 need notification counter
                0
        );
        menuGridItems.add(gridItem);

        MenuGridViewModel menuGrid = new MenuGridViewModel();
        menuGrid.setTitle(context.getString(R.string.title_menu_shopping_transaction));
        menuGrid.setLinkText(context.getString(R.string.label_menu_show_tx_history));
        menuGrid.setApplinkUrl("tokopedia://buyer");
        menuGrid.setItems(menuGridItems);
        items.add(menuGrid);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_complaint));
        menuList.setMenuDescription(context.getString(R.string.label_menu_complaint));
        menuList.setApplink(ApplinkConst.RESCENTER_BUYER);
        items.add(menuList);

        menuGrid = new MenuGridViewModel();
        menuGrid.setTitle(context.getString(R.string.title_menu_other_transaction));
        menuGridItems = new ArrayList<>();
        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.title_menu_top_up_bill),
                ApplinkConst.DIGITAL_ORDER,
                0
        );
        menuGridItems.add(gridItem);
        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.title_menu_deals),
                ApplinkConst.DEALS_ORDER,
                0
        );
        menuGridItems.add(gridItem);
        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.title_menu_flight),
                ApplinkConst.FLIGHT_ORDER,
                0
        );
        menuGridItems.add(gridItem);
        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_tokocash_green,
                context.getString(R.string.title_menu_show_all),
                // TODO: 7/25/18 need applink
                "",
                0
        );
        menuGridItems.add(gridItem);
        menuGrid.setItems(menuGridItems);
        items.add(menuGrid);

        menuTitle = new MenuTitleViewModel();
        menuTitle.setTitle(context.getString(R.string.title_menu_favorites));
        items.add(menuTitle);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_last_seen));
        menuList.setMenuDescription(context.getString(R.string.label_menu_last_seen));
        // TODO: 7/25/18 need applink
        menuList.setApplink("");
        items.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_favorite_shops));
        menuList.setMenuDescription(context.getString(R.string.label_menu_favorite_shops));
        // TODO: 7/25/18 need applink
        menuList.setApplink("");
        items.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_top_up_bill_subscription));
        menuList.setMenuDescription(context.getString(R.string.label_menu_top_up_bill_subscription));
        // TODO: 7/25/18 need applink
        menuList.setApplink("");
        items.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_top_up_numbers));
        menuList.setMenuDescription(context.getString(R.string.label_menu_top_up_numbers));
        // TODO: 7/25/18 need applink
        menuList.setApplink("");
        items.add(menuList);

        InfoCardViewModel infoCard = new InfoCardViewModel();
        infoCard.setMainText(context.getString(R.string.title_menu_wallet_referral));
        infoCard.setSecondaryText(context.getString(R.string.label_menu_wallet_referral));
        infoCard.setApplink(ApplinkConst.REFERRAL);
        items.add(infoCard);

        menuTitle = new MenuTitleViewModel();
        menuTitle.setTitle(context.getString(R.string.title_menu_help));
        items.add(menuTitle);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_resolution_center));
        menuList.setMenuDescription(context.getString(R.string.label_menu_resolution_center));
        // TODO: 7/25/18 need applink
        menuList.setApplink("");
        items.add(menuList);

        BuyerViewModel model = new BuyerViewModel();
        model.setItems(items);

        return model;
    }

    private SellerViewModel getSellerModel(AccountModel accountModel) {
        return new SellerViewModel();
    }
}
