package com.tokopedia.home.account.data.mapper;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuTitleViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

import static com.tokopedia.home.account.AccountConstants.Analytics.PEMBELI;

/**
 * @author by alvinatin on 10/08/18.
 */

public class BuyerAccountMapper implements Func1<GraphqlResponse, BuyerViewModel>{
    private Context context;

    @Inject
    public BuyerAccountMapper(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public BuyerViewModel call(GraphqlResponse graphqlResponse) {
        AccountModel accountModel = graphqlResponse.getData(AccountModel.class);
        return getBuyerModel(context, accountModel);
    }

    private static BuyerViewModel getBuyerModel(Context context, AccountModel accountModel) {
        BuyerViewModel model = new BuyerViewModel();
        List<ParcelableViewModel> items = new ArrayList<>();

        BuyerCardViewModel buyerCardViewModel = new BuyerCardViewModel();
        buyerCardViewModel.setUserId(accountModel.getProfile().getUserId());
        buyerCardViewModel.setName(accountModel.getProfile().getFullName());
        buyerCardViewModel.setTokopoint(accountModel.getTokopoints().getStatus().getPoints().getRewardStr());
        buyerCardViewModel.setCoupons(accountModel.getTokopointsSumCoupon().getSumCoupon());
        buyerCardViewModel.setImageUrl(accountModel.getProfile().getProfilePicture());
        buyerCardViewModel.setProgress(accountModel.getProfile().getCompletion());
        items.add(buyerCardViewModel);

        TokopediaPayViewModel tokopediaPayViewModel = new TokopediaPayViewModel();
        if (!accountModel.getWallet().isLinked()){
            tokopediaPayViewModel.setLabelLeft(context.getString(R.string.label_tokopedia_pay_wallet));
            tokopediaPayViewModel.setAmountLeft(context.getString(R.string.label_wallet_activation));
            tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getAction().getApplink());
        } else {
            tokopediaPayViewModel.setLabelLeft(context.getString(R.string.label_tokopedia_pay_wallet));
            tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getBalance());
            tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getApplink());
        }
        tokopediaPayViewModel.setLabelRight(context.getString(R.string.label_tokopedia_pay_deposit));
        tokopediaPayViewModel.setAmountRight(accountModel.getDeposit().getDepositFmt());
        tokopediaPayViewModel.setApplinkRight(ApplinkConst.DEPOSIT);
        items.add(tokopediaPayViewModel);

        MenuTitleViewModel menuTitle = new MenuTitleViewModel();
        menuTitle.setTitle(context.getString(R.string.title_menu_transaction));
        items.add(menuTitle);

        MenuListViewModel menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_waiting_for_payment));
        menuList.setMenuDescription(context.getString(R.string.label_menu_waiting_for_payment));
        try {
            menuList.setCount(Integer.parseInt(accountModel.getNotifications().getBuyerOrder().getPaymentStatus()));
        } catch (NumberFormatException e) {
            menuList.setCount(0);
        }
        menuList.setApplink(ApplinkConst.PMS);
        menuList.setTitleTrack(PEMBELI);
        menuList.setSectionTrack(context.getString(R.string.title_menu_transaction));
        items.add(menuList);

        MenuGridViewModel menuGrid = new MenuGridViewModel();
        menuGrid.setTitle(context.getString(R.string.title_menu_shopping_transaction));
        menuGrid.setLinkText(context.getString(R.string.label_menu_show_history));
        menuGrid.setTitleTrack(PEMBELI);
        menuGrid.setSectionTrack(context.getString(R.string.title_menu_transaction));
        menuGrid.setApplinkUrl(ApplinkConst.PURCHASE_HISTORY);

        List<MenuGridItemViewModel> menuGridItems = new ArrayList<>();
        MenuGridItemViewModel gridItem = new MenuGridItemViewModel(
                R.drawable.ic_waiting_for_confirmation,
                context.getString(R.string.label_menu_waiting_confirmation),
                ApplinkConst.PURCHASE_CONFIRMED,
                accountModel.getNotifications().getBuyerOrder().getConfirmed(),
                PEMBELI,
                context.getString(R.string.title_menu_transaction));
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_order_processed,
                context.getString(R.string.label_menu_order_processed),
                ApplinkConst.PURCHASE_PROCESSED,
                accountModel.getNotifications().getBuyerOrder().getProcessed(),
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_shipped,
                context.getString(R.string.label_menu_shipping),
                ApplinkConst.PURCHASE_SHIPPED,
                accountModel.getNotifications().getBuyerOrder().getShipped(),
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_delivered,
                context.getString(R.string.label_menu_delivered),
                ApplinkConst.PURCHASE_DELIVERED,
                accountModel.getNotifications().getBuyerOrder().getArriveAtDestination(),
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);

        menuGrid.setItems(menuGridItems);
        items.add(menuGrid);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_buyer_complain));
        menuList.setMenuDescription(context.getString(R.string.label_menu_buyer_complain));
        menuList.setCount(accountModel.getNotifications().getResolution().getBuyer());
        menuList.setApplink(ApplinkConst.RESCENTER_BUYER);
        menuList.setTitleTrack(PEMBELI);
        menuList.setSectionTrack(context.getString(R.string.title_menu_transaction));
        items.add(menuList);

        menuGrid = new MenuGridViewModel();
        menuGrid.setTitle(context.getString(R.string.title_menu_other_transaction));
        menuGridItems = new ArrayList<>();
        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_top_up_bill,
                context.getString(R.string.title_menu_top_up_bill),
                ApplinkConst.DIGITAL_ORDER,
                0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);
        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_flight,
                context.getString(R.string.title_menu_flight),
                ApplinkConst.FLIGHT_ORDER,
                0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_train,
                context.getString(R.string.title_menu_train),
                AccountConstants.Navigation.TRAIN_ORDER_LIST,
                0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_see_all,
                context.getString(R.string.title_menu_show_all),
                AccountConstants.Navigation.SEE_ALL,
                0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
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
        menuList.setApplink(ApplinkConst.RECENT_VIEW);
        menuList.setTitleTrack(PEMBELI);
        menuList.setSectionTrack(context.getString(R.string.title_menu_favorites));
        items.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_wishlist));
        menuList.setMenuDescription(context.getString(R.string.label_menu_wishlist));
        menuList.setApplink(ApplinkConst.WISHLIST);
        menuList.setTitleTrack(PEMBELI);
        menuList.setSectionTrack(context.getString(R.string.title_menu_favorites));
        items.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_favorite_shops));
        menuList.setMenuDescription(context.getString(R.string.label_menu_favorite_shops));
        menuList.setApplink(ApplinkConst.FAVORITE);
        menuList.setTitleTrack(PEMBELI);
        menuList.setSectionTrack(context.getString(R.string.title_menu_favorites));
        items.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_mybills));
        menuList.setMenuDescription(context.getString(R.string.label_menu_mybills));
        menuList.setApplink(String.format("%s?url=%s",
                ApplinkConst.WEBVIEW,
                AccountConstants.Url.Pulsa.MYBILLS));
        menuList.setTitleTrack(PEMBELI);
        menuList.setSectionTrack(context.getString(R.string.title_menu_mybills));
        items.add(menuList);

//        menuList = new MenuListViewModel();
//        menuList.setMenu(context.getString(R.string.title_menu_top_up_bill_subscription));
//        menuList.setMenuDescription(context.getString(R.string.label_menu_top_up_bill_subscription));
//        menuList.setApplink(String.format("%s?url=%s",
//                ApplinkConst.WEBVIEW,
//                AccountConstants.Url.Pulsa.PULSA_SUBSCRIBE));
//        menuList.setTitleTrack(PEMBELI);
//        menuList.setSectionTrack(context.getString(R.string.title_menu_favorites));
//        items.add(menuList);
//
//        menuList = new MenuListViewModel();
//        menuList.setMenu(context.getString(R.string.title_menu_top_up_numbers));
//        menuList.setMenuDescription(context.getString(R.string.label_menu_top_up_numbers));
//        menuList.setApplink(String.format("%s?url=%s",
//                ApplinkConst.WEBVIEW,
//                AccountConstants.Url.Pulsa.PULSA_FAV_NUMBER));
//        menuList.setTitleTrack(PEMBELI);
//        menuList.setSectionTrack(context.getString(R.string.title_menu_favorites));
//        items.add(menuList);

        InfoCardViewModel infoCard = new InfoCardViewModel();
        infoCard.setIconRes(R.drawable.ic_tokocash_big);
        infoCard.setMainText(context.getString(R.string.title_menu_wallet_referral));
        infoCard.setSecondaryText(context.getString(R.string.label_menu_wallet_referral));
        infoCard.setApplink(ApplinkConst.REFERRAL);
        infoCard.setTitleTrack(PEMBELI);
        infoCard.setSectionTrack(context.getString(R.string.title_menu_wallet_referral));
        items.add(infoCard);

        menuTitle = new MenuTitleViewModel();
        menuTitle.setTitle(context.getString(R.string.title_menu_help));
        items.add(menuTitle);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_resolution_center));
        menuList.setMenuDescription(context.getString(R.string.label_menu_resolution_center));
        menuList.setApplink(ApplinkConst.CONTACT_US_NATIVE);
        menuList.setTitleTrack(PEMBELI);
        menuList.setSectionTrack(context.getString(R.string.title_menu_help));
        items.add(menuList);

        model.setItems(items);

        return model;
    }
}
