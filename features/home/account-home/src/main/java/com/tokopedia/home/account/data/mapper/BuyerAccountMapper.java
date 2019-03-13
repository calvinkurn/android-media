package com.tokopedia.home.account.data.mapper;

import android.content.Context;
import android.view.View;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.presentation.util.AccountByMeHelper;
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuTitleViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayBSModel;
import com.tokopedia.home.account.presentation.viewmodel.TokopediaPayViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

import static com.tokopedia.home.account.AccountConstants.Analytics.BY_ME;
import static com.tokopedia.home.account.AccountConstants.Analytics.CLICK_CHALLENGE;
import static com.tokopedia.home.account.AccountConstants.Analytics.PEMBELI;

/**
 * @author by alvinatin on 10/08/18.
 */

public class BuyerAccountMapper implements Func1<AccountModel, BuyerViewModel> {
    private static final String OVO = "OVO";
    private Context context;

    @Inject
    public BuyerAccountMapper(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public BuyerViewModel call(AccountModel accountModel) {
        return getBuyerModel(context, accountModel);
    }

    private BuyerViewModel getBuyerModel(Context context, AccountModel accountModel) {
        BuyerViewModel model = new BuyerViewModel();
        List<ParcelableViewModel> items = new ArrayList<>();

        if(accountModel.getProfile() != null) {
            items.add(getBuyerProfileMenu(accountModel));
        }

        String cdnUrl = AccountHomeUrl.CDN_URL;
        if (context.getApplicationContext() instanceof AccountHomeRouter) {
            cdnUrl = ((AccountHomeRouter) context.getApplicationContext())
                    .getStringRemoteConfig(AccountHomeUrl.ImageUrl.KEY_IMAGE_HOST, AccountHomeUrl.CDN_URL);
        }

        TokopediaPayViewModel tokopediaPayViewModel = new TokopediaPayViewModel();
        if(accountModel.getWallet() != null) {
            tokopediaPayViewModel.setLinked(accountModel.getWallet().isLinked());
            tokopediaPayViewModel.setWalletType(accountModel.getWallet().getWalletType());
            if (accountModel.getWallet().getWalletType().equals(OVO)) {
                tokopediaPayViewModel.setIconUrlLeft(AccountConstants.ImageUrl.OVO_IMG);
                if (!accountModel.getWallet().isLinked()) {
                    if (accountModel.getWallet().getAmountPendingCashback() > 0) {
                        tokopediaPayViewModel.setLabelLeft("(+" + accountModel.getWallet().getPendingCashback() + ")");
                    } else {
                        tokopediaPayViewModel.setLabelLeft(accountModel.getWallet().getText());
                    }

                    if(accountModel.getWallet().getAction() != null) {
                        tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getAction().getText());
                        tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getAction().getApplink());
                    }
                } else {
                    tokopediaPayViewModel.setLabelLeft("Points " + accountModel.getWallet().getPointBalance());
                    tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getCashBalance());
                    tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getApplink());
                }
            } else {
                tokopediaPayViewModel.setIconUrlLeft(cdnUrl + AccountHomeUrl.ImageUrl.TOKOCASH_IMG);
                if (!accountModel.getWallet().isLinked()) {
                    tokopediaPayViewModel.setLabelLeft(accountModel.getWallet().getText());
                    if(accountModel.getWallet().getAction() != null) {
                        tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getAction().getText());
                        tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getAction().getApplink());
                    }
                } else {
                    tokopediaPayViewModel.setLabelLeft(accountModel.getWallet().getText());
                    tokopediaPayViewModel.setAmountLeft(accountModel.getWallet().getBalance());
                    tokopediaPayViewModel.setApplinkLeft(accountModel.getWallet().getApplink());
                }
            }
        }

        if (!((AccountHomeRouter) context.getApplicationContext()).getBooleanRemoteConfig("mainapp_android_enable_tokocard", false)
                || accountModel.getVccUserStatus() == null
                || accountModel.getVccUserStatus().getStatus() == null
                || accountModel.getVccUserStatus().getStatus().equalsIgnoreCase(AccountConstants.VccStatus.NOT_FOUND)
                || accountModel.getVccUserStatus().getStatus().equalsIgnoreCase(AccountConstants.VccStatus.NOT_ELIGIBLE)) {
            tokopediaPayViewModel.setIconUrlRight(cdnUrl+AccountHomeUrl.ImageUrl.SALDO_IMG);
            tokopediaPayViewModel.setLabelRight(context.getString(R.string.label_tokopedia_pay_deposit));
            tokopediaPayViewModel.setAmountRight(accountModel.getDeposit().getDepositFmt());
            tokopediaPayViewModel.setApplinkRight(ApplinkConst.DEPOSIT);
            items.add(tokopediaPayViewModel);
        } else {
            TokopediaPayBSModel bsDataRight = new TokopediaPayBSModel();
            tokopediaPayViewModel.setLabelRight(accountModel.getVccUserStatus().getTitle());
            tokopediaPayViewModel.setIconUrlRight(accountModel.getVccUserStatus().getIcon());
            tokopediaPayViewModel.setVccUserStatus(accountModel.getVccUserStatus().getStatus());
            if (accountModel.getVccUserStatus().getStatus().equalsIgnoreCase(AccountConstants.VccStatus.ACTIVE)) {
                tokopediaPayViewModel.setAmountRight(CurrencyFormatUtil.convertPriceValueToIdrFormat(Long.parseLong(accountModel.getVccUserStatus().getBody()), true));
            } else {
                tokopediaPayViewModel.setAmountRight(accountModel.getVccUserStatus().getBody());
            }

            switch (accountModel.getVccUserStatus().getStatus()) {
                case AccountConstants.VccStatus.BLOCKED:
                case AccountConstants.VccStatus.ELIGIBLE:
                case AccountConstants.VccStatus.DEACTIVATED:
                    tokopediaPayViewModel.setRightImportant(true);
                    break;
            }

            if (!"".equalsIgnoreCase(accountModel.getVccUserStatus().getRedirectionUrl())) {
                tokopediaPayViewModel.setApplinkRight(accountModel.getVccUserStatus().getRedirectionUrl());
            }

            if (!"".equalsIgnoreCase(accountModel.getVccUserStatus().getMessageHeader())) {
                bsDataRight.setTitle(accountModel.getVccUserStatus().getMessageHeader());
            }

            if (!"".equalsIgnoreCase(accountModel.getVccUserStatus().getMessageBody())) {
                bsDataRight.setBody(accountModel.getVccUserStatus().getMessageBody());
            }

            if (!"".equalsIgnoreCase(accountModel.getVccUserStatus().getMessageButtonName())) {
                bsDataRight.setButtonText(accountModel.getVccUserStatus().getMessageButtonName());
            }

            if (!"".equalsIgnoreCase(accountModel.getVccUserStatus().getMessageUrl())) {
                bsDataRight.setButtonRedirectionUrl(accountModel.getVccUserStatus().getMessageUrl());
            }

            tokopediaPayViewModel.setBsDataRight(bsDataRight);
            items.add(tokopediaPayViewModel);
        }


        MenuTitleViewModel menuTitle = new MenuTitleViewModel();
        menuTitle.setTitle(context.getString(R.string.title_menu_transaction));
        items.add(menuTitle);

        MenuListViewModel menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_waiting_for_payment));
        menuList.setMenuDescription(context.getString(R.string.label_menu_waiting_for_payment));
        try {
            menuList.setCount(Integer.parseInt(accountModel.getNotifications().getBuyerOrder().getPaymentStatus()));
        } catch (Exception e) {
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
        menuGrid.setApplinkUrl(ApplinkConst.MARKETPLACE_ORDER);
        if (((AccountHomeRouter) context.getApplicationContext()).getBooleanRemoteConfig(RemoteConfigKey.APP_GLOBAL_NAV_NEW_DESIGN, true)) {
            menuGrid.setItems(getMarketPlaceOrderMenu(
                    accountModel.getNotifications() != null && accountModel.getNotifications().getBuyerOrder() != null,
                    accountModel
            ));
        }else {

            menuGrid.setItems(getBuyerOrderMenu(
                    accountModel.getNotifications() != null && accountModel.getNotifications().getBuyerOrder() != null,
                    accountModel
            ));
        }


        menuGrid = new MenuGridViewModel();
        menuGrid.setTitle(context.getString(R.string.title_menu_other_transaction_1));
        menuGrid.setItems(getDigitalOrderMenu());
        items.add(menuGrid);

        items.add(getBuyerResolutionMenu(accountModel));

        menuTitle = new MenuTitleViewModel();
        menuTitle.setTitle(context.getString(R.string.title_menu_favorites));
        items.add(menuTitle);

        if (context.getApplicationContext() instanceof AccountHomeRouter
                && ((AccountHomeRouter) context.getApplicationContext()).isEnableInterestPick()){
            menuList = new MenuListViewModel();
            menuList.setMenu(context.getString(R.string.title_menu_favorite_topic));
            menuList.setMenuDescription(context.getString(R.string.label_menu_favorite_topic));
            menuList.setApplink(ApplinkConst.INTEREST_PICK);
            menuList.setTitleTrack(PEMBELI);
            menuList.setSectionTrack(context.getString(R.string.title_menu_favorites));
            items.add(menuList);
        }

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
                AccountHomeUrl.Pulsa.MYBILLS));
        menuList.setTitleTrack(PEMBELI);
        menuList.setSectionTrack(context.getString(R.string.title_menu_mybills));
        items.add(menuList);

        if (((AccountHomeRouter) context.getApplicationContext()).getBooleanRemoteConfig("app_show_referral_button", false)) {
           String title=((AccountHomeRouter) context.getApplicationContext()).getStringRemoteConfig("app_referral_title", context.getString(R.string.title_menu_wallet_referral));
            String subTitle=((AccountHomeRouter) context.getApplicationContext()).getStringRemoteConfig("app_referral_subtitle", context.getString(R.string.label_menu_wallet_referral));
            InfoCardViewModel infoCard = new InfoCardViewModel();
            infoCard.setIconRes(R.drawable.ic_tokocash_big);
            infoCard.setMainText(title);
            infoCard.setSecondaryText(subTitle);
            infoCard.setApplink(ApplinkConst.REFERRAL);
            infoCard.setTitleTrack(PEMBELI);
            infoCard.setSectionTrack(context.getString(R.string.title_menu_wallet_referral));
            items.add(infoCard);
        }

        if (((AccountHomeRouter) context.getApplicationContext()).getBooleanRemoteConfig("app_enable_indi_challenges", true)) {
            InfoCardViewModel infoCard = new InfoCardViewModel();
            infoCard.setIconRes(R.drawable.ic_challenge_trophy);
            infoCard.setMainText(context.getString(R.string.title_menu_challenge));
            infoCard.setSecondaryText(context.getString(R.string.label_menu_challenge));
            infoCard.setApplink(ApplinkConst.CHALLENGE);
            infoCard.setTitleTrack(PEMBELI);
            infoCard.setSectionTrack(CLICK_CHALLENGE);
            if (context.getApplicationContext() instanceof AccountHomeRouter
                    && ((AccountHomeRouter) context.getApplicationContext()).getBooleanRemoteConfig(RemoteConfigKey.APP_ENTRY_CHALLENGE_BARU, true))
                infoCard.setNewTxtVisiblle(View.VISIBLE);
            items.add(infoCard);
        }

        if (((AccountHomeRouter) context.getApplicationContext()).getBooleanRemoteConfig(RemoteConfigKey.APP_ENABLE_ACCOUNT_AFFILIATE, true)) {
            InfoCardViewModel infoCard = new InfoCardViewModel();

            if (AccountByMeHelper.isFirstTimeByme(context)) {
                infoCard.setIconRes(R.drawable.ic_byme_card_notif);
            } else {
                infoCard.setIconRes(R.drawable.ic_byme_card);
            }

            infoCard.setMainText(context.getString(R.string.title_menu_affiliate));
            infoCard.setSecondaryText(context.getString(R.string.label_menu_affiliate));
            infoCard.setApplink(ApplinkConst.AFFILIATE_EXPLORE);
            infoCard.setTitleTrack(PEMBELI);
            infoCard.setSectionTrack(BY_ME);
            items.add(infoCard);
        }

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

    private BuyerCardViewModel getBuyerProfileMenu(AccountModel accountModel) {
        BuyerCardViewModel buyerCardViewModel = new BuyerCardViewModel();
        buyerCardViewModel.setUserId(accountModel.getProfile().getUserId());
        buyerCardViewModel.setName(accountModel.getProfile().getFullName());

        if(accountModel.getTokopoints() != null
                && accountModel.getTokopoints().getStatus() != null
                && accountModel.getTokopoints().getStatus().getPoints() != null) {
            buyerCardViewModel.setTokopoint(accountModel.getTokopoints().getStatus().getPoints().getRewardStr());
        }

        if(accountModel.getTokopointsSumCoupon() != null) {
            buyerCardViewModel.setCoupons(accountModel.getTokopointsSumCoupon().getSumCoupon());
        }

        buyerCardViewModel.setImageUrl(accountModel.getProfile().getProfilePicture());
        if(accountModel.getProfile().getCompletion() != null) {
            buyerCardViewModel.setProgress(accountModel.getProfile().getCompletion());
        }
        buyerCardViewModel.setAffiliate(accountModel.isAffiliate());

        return buyerCardViewModel;
    }

    private List<MenuGridItemViewModel> getBuyerOrderMenu(Boolean isNotNull, AccountModel accountModel) {
        List<MenuGridItemViewModel> menuGridItems = new ArrayList<>();
        MenuGridItemViewModel gridItem = new MenuGridItemViewModel(
                R.drawable.ic_waiting_for_confirmation,
                context.getString(R.string.label_menu_waiting_confirmation),
                ApplinkConst.PURCHASE_CONFIRMED,
                isNotNull ? accountModel.getNotifications().getBuyerOrder().getConfirmed() : 0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction));
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_order_processed,
                context.getString(R.string.label_menu_order_processed),
                ApplinkConst.PURCHASE_PROCESSED,
                isNotNull ? accountModel.getNotifications().getBuyerOrder().getProcessed() : 0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_shipped,
                context.getString(R.string.label_menu_shipping),
                ApplinkConst.PURCHASE_SHIPPED,
                isNotNull ? accountModel.getNotifications().getBuyerOrder().getShipped() : 0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_delivered,
                context.getString(R.string.label_menu_delivered),
                ApplinkConst.PURCHASE_DELIVERED,
                isNotNull ? accountModel.getNotifications().getBuyerOrder().getArriveAtDestination() : 0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);
        return menuGridItems;
    }

    private List<MenuGridItemViewModel> getMarketPlaceOrderMenu(Boolean isNotNull, AccountModel accountModel) {
        List<MenuGridItemViewModel> menuGridItems = new ArrayList<>();
        MenuGridItemViewModel gridItem = new MenuGridItemViewModel(
                R.drawable.ic_waiting_for_confirmation,
                context.getString(R.string.label_menu_waiting_confirmation),
                ApplinkConst.MARKETPLACE_WAITING_CONFIRMATION,
                isNotNull ? accountModel.getNotifications().getBuyerOrder().getConfirmed() : 0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction));
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_order_processed,
                context.getString(R.string.label_menu_order_processed),
                ApplinkConst.MARKETPLACE_ORDER_PROCESSED,
                isNotNull ? accountModel.getNotifications().getBuyerOrder().getProcessed() : 0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_shipped,
                context.getString(R.string.label_menu_shipping),
                ApplinkConst.MARKETPLACE_SENT,
                isNotNull ? accountModel.getNotifications().getBuyerOrder().getShipped() : 0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_delivered,
                context.getString(R.string.label_menu_delivered),
                ApplinkConst.MARKETPLACE_DELIVERED,
                isNotNull ? accountModel.getNotifications().getBuyerOrder().getArriveAtDestination() : 0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);
        return menuGridItems;
    }
    private ParcelableViewModel getBuyerResolutionMenu(AccountModel accountModel) {
        MenuListViewModel menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_buyer_complain));
        menuList.setMenuDescription(context.getString(R.string.label_menu_buyer_complain));
        if(accountModel.getNotifications() != null && accountModel.getNotifications().getResolution() != null) {
            menuList.setCount(accountModel.getNotifications().getResolution().getBuyer());
        }
        menuList.setApplink(ApplinkConst.RESCENTER_BUYER);
        menuList.setTitleTrack(PEMBELI);
        menuList.setSectionTrack(context.getString(R.string.title_menu_transaction));

        return menuList;
    }

    private List<MenuGridItemViewModel> getDigitalOrderMenu() {
        List<MenuGridItemViewModel> menuGridItems = new ArrayList<>();
        MenuGridItemViewModel gridItem =  null;
        if (((AccountHomeRouter) context.getApplicationContext()).getBooleanRemoteConfig(RemoteConfigKey.APP_GLOBAL_NAV_NEW_DESIGN, true)) {
            gridItem = new MenuGridItemViewModel(
                    R.drawable.ic_belanja,
                    context.getString(R.string.title_menu_market_place),
                    ApplinkConst.MARKETPLACE_ORDER,
                    0,
                    PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            );
        }else {

            gridItem = new MenuGridItemViewModel(
                    R.drawable.ic_belanja,
                    context.getString(R.string.title_menu_market_place),
                    ApplinkConst.PURCHASE_HISTORY,
                    0,
                    PEMBELI,
                    context.getString(R.string.title_menu_transaction)
            );
        }


        menuGridItems.add(gridItem);
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
                R.drawable.ic_see_all,
                context.getString(R.string.title_menu_show_all),
                AccountConstants.Navigation.SEE_ALL,
                0,
                PEMBELI,
                context.getString(R.string.title_menu_transaction)
        );
        menuGridItems.add(gridItem);

        return menuGridItems;
    }
}