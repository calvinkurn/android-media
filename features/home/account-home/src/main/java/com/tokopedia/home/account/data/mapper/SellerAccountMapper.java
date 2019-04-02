package com.tokopedia.home.account.data.mapper;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.presentation.viewmodel.AddProductViewModel;
import com.tokopedia.home.account.presentation.viewmodel.InfoCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuListViewModel;
import com.tokopedia.home.account.presentation.viewmodel.MenuTitleViewModel;
import com.tokopedia.home.account.presentation.viewmodel.SellerEmptyViewModel;
import com.tokopedia.home.account.presentation.viewmodel.SellerSaldoViewModel;
import com.tokopedia.home.account.presentation.viewmodel.ShopCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.TickerViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;
import com.tokopedia.navigation_common.model.DepositModel;
import com.tokopedia.navigation_common.model.SaldoModel;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.topads.common.data.model.DataDeposit;
import com.tokopedia.user_identification_common.KYCConstant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

import static com.tokopedia.home.account.AccountConstants.Analytics.LOAN;
import static com.tokopedia.home.account.AccountConstants.Analytics.PENJUAL;

/**
 * @author by alvinatin on 10/08/18.
 */

public class SellerAccountMapper implements Func1<GraphqlResponse, SellerViewModel> {

    private Context context;

    @Inject
    public SellerAccountMapper(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public SellerViewModel call(GraphqlResponse graphqlResponse) {
        AccountModel accountModel = graphqlResponse.getData(AccountModel.class);
        SaldoModel saldoModel = graphqlResponse.getData(SaldoModel.class);
        accountModel.setSaldoModel(saldoModel);
        DataDeposit.Response dataDepositResponse = graphqlResponse.getData(DataDeposit.Response.class);
        DataDeposit dataDeposit = null;
        if (graphqlResponse.getError(DataDeposit.Response.class) == null || graphqlResponse.getError(DataDeposit.Response.class).isEmpty()) {
            dataDeposit = dataDepositResponse.getDataResponse().getDataDeposit();
        }
        SellerViewModel sellerViewModel;
        if (accountModel.getShopInfo() != null
                && accountModel.getShopInfo().getInfo() != null
                && !TextUtils.isEmpty(accountModel.getShopInfo().getInfo().getShopId())
                && !accountModel.getShopInfo().getInfo().getShopId().equalsIgnoreCase("-1")) {
            sellerViewModel = getSellerModel(context, accountModel, dataDeposit);
            sellerViewModel.setSeller(true);
        } else {
            sellerViewModel = getEmptySellerModel();
            sellerViewModel.setSeller(false);
        }

        return sellerViewModel;
    }

    private SellerViewModel getSellerModel(Context context, AccountModel accountModel, DataDeposit dataDeposit) {
        SellerViewModel sellerViewModel = new SellerViewModel();
        List<ParcelableViewModel> items = new ArrayList<>();

        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);

        boolean showPinjamanModalOnTop = remoteConfig
                .getBoolean(RemoteConfigKey.PINJAMAN_MODAL_AKUN_PAGE_POSITION_TOP, false);

        String mitraTopperMaxLoan = "";
        String mitraTopperUrl = "";
        if (accountModel.getLePreapprove() != null &&
                accountModel.getLePreapprove().getFieldData() != null &&
                accountModel.getLePreapprove().getFieldData().getPreApp() != null) {
            mitraTopperMaxLoan = accountModel.getLePreapprove().getFieldData().getPreApp().getPartnerMaxLoan();
            mitraTopperUrl = accountModel.getLePreapprove().getFieldData().getUrl();
        }

        try {
            Long loan = Long.parseLong(mitraTopperMaxLoan);
            if (loan > 0) {
                mitraTopperMaxLoan = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        Long.parseLong(mitraTopperMaxLoan),
                        true);
            }
        } catch (NumberFormatException e) { /*ignore*/ }

        TickerViewModel tickerViewModel = parseTickerSeller(context, accountModel);
        if (tickerViewModel != null && !tickerViewModel.getListMessage().isEmpty()) {
            items.add(tickerViewModel);
        }

        if (accountModel.getShopInfo() != null && accountModel.getShopInfo().getInfo() != null) {
            items.add(getShopInfoMenu(accountModel, dataDeposit));
        }

        if (accountModel.getSaldoModel().getSaldo().getDepositLong() != 0) {
            items.add(getSaldoInfo(accountModel.getSaldoModel().getSaldo()));
        }

        if (showPinjamanModalOnTop) {
            if (!mitraTopperMaxLoan.isEmpty() && !mitraTopperMaxLoan.equals("0") && !mitraTopperUrl.isEmpty()) {
                InfoCardViewModel infoCardViewModel = new InfoCardViewModel();
                infoCardViewModel.setIconRes(R.drawable.ic_personal_loan);
                infoCardViewModel.setTitleTrack(PENJUAL);
                infoCardViewModel.setSectionTrack(LOAN);
                infoCardViewModel.setItemTrack(LOAN);
                infoCardViewModel.setMainText(context.getString(R.string.title_menu_loan));
                infoCardViewModel.setSecondaryText(String.format("%s %s", context.getString(R.string.label_menu_loan), mitraTopperMaxLoan));
                infoCardViewModel.setApplink(String.format("%s?url=%s", ApplinkConst.WEBVIEW, mitraTopperUrl));
                items.add(infoCardViewModel);
            }
        }

        MenuGridViewModel menuGrid = new MenuGridViewModel();
        menuGrid.setTitle(context.getString(R.string.title_menu_sales));
        menuGrid.setLinkText(context.getString(R.string.label_menu_show_history));
        menuGrid.setApplinkUrl(ApplinkConst.SELLER_HISTORY);
        menuGrid.setTitleTrack(PENJUAL);
        menuGrid.setSectionTrack(context.getString(R.string.title_menu_sales));


        menuGrid.setItems(getSellerOrderMenu(
                accountModel.getNotifications() != null && accountModel.getNotifications().getSellerOrder() != null,
                accountModel
        ));
        items.add(menuGrid);

        items.add(getSellerResolutionMenu(accountModel));

        MenuTitleViewModel menuTitle = new MenuTitleViewModel(context.getString(R.string.title_menu_product));
        items.add(menuTitle);

        items.add(new AddProductViewModel());

        MenuListViewModel menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_product_list));
        menuList.setMenuDescription(context.getString(R.string.label_menu_product_list));
        menuList.setApplink(ApplinkConst.PRODUCT_MANAGE);
        menuList.setTitleTrack(PENJUAL);
        menuList.setSectionTrack(context.getString(R.string.title_menu_product));
        items.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_product_feature));
        menuList.setMenuDescription(context.getString(R.string.label_menu_product_feature));
        menuList.setApplink(AccountConstants.Navigation.FEATURED_PRODUCT);
        menuList.setTitleTrack(PENJUAL);
        menuList.setSectionTrack(context.getString(R.string.title_menu_product));
        items.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_product_draft));
        menuList.setMenuDescription(context.getString(R.string.label_menu_product_draft));
        menuList.setApplink(ApplinkConst.PRODUCT_DRAFT);
        menuList.setTitleTrack(PENJUAL);
        menuList.setSectionTrack(context.getString(R.string.title_menu_product));
        items.add(menuList);

        menuTitle = new MenuTitleViewModel(context.getString(R.string.title_menu_other_features));
        items.add(menuTitle);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_opportunity));
        menuList.setMenuDescription(context.getString(R.string.label_menu_opportunity));
        menuList.setApplink(ApplinkConst.SELLER_OPPORTUNITY);
        menuList.setTitleTrack(PENJUAL);
        menuList.setSectionTrack(context.getString(R.string.title_menu_other_features));
        items.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_topads));
        menuList.setMenuDescription(context.getString(R.string.label_menu_topads));
        menuList.setApplink(AccountConstants.Navigation.TOPADS);
        menuList.setTitleTrack(PENJUAL);
        menuList.setSectionTrack(context.getString(R.string.title_menu_other_features));
        items.add(menuList);

        menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_seller_center));
        menuList.setMenuDescription(context.getString(R.string.label_menu_seller_center));
        menuList.setApplink(ApplinkConst.SELLER_CENTER);
        menuList.setTitleTrack(PENJUAL);
        menuList.setSectionTrack(context.getString(R.string.title_menu_other_features));
        items.add(menuList);

        if (!showPinjamanModalOnTop) {
            if (!mitraTopperMaxLoan.isEmpty() && !mitraTopperMaxLoan.equals("0") && !mitraTopperUrl.isEmpty()) {
                InfoCardViewModel infoCardViewModel = new InfoCardViewModel();
                infoCardViewModel.setIconRes(R.drawable.ic_personal_loan);
                infoCardViewModel.setTitleTrack(PENJUAL);
                infoCardViewModel.setSectionTrack(LOAN);
                infoCardViewModel.setItemTrack(LOAN);
                infoCardViewModel.setMainText(context.getString(R.string.title_menu_loan));
                infoCardViewModel.setSecondaryText(String.format("%s %s", context.getString(R.string.label_menu_loan), mitraTopperMaxLoan));
                infoCardViewModel.setApplink(String.format("%s?url=%s", ApplinkConst.WEBVIEW, mitraTopperUrl));
                items.add(infoCardViewModel);
            }
        }

        sellerViewModel.setItems(items);
        return sellerViewModel;
    }

    private TickerViewModel parseTickerSeller(Context context, AccountModel accountModel) {
        TickerViewModel sellerTickerModel = new TickerViewModel(new ArrayList<>());

        if (accountModel.getKycStatusPojo() != null
                && accountModel.getKycStatusPojo().getKycStatusDetailPojo() != null
                && accountModel.getKycStatusPojo().getKycStatusDetailPojo()
                .getIsSuccess() == KYCConstant.IS_SUCCESS_GET_STATUS
                && accountModel.getKycStatusPojo().getKycStatusDetailPojo()
                .getStatus() == KYCConstant.STATUS_NOT_VERIFIED) {
            sellerTickerModel.getListMessage().add(context.getString(R.string.ticker_unverified));
        }

        return sellerTickerModel;

    }

    private void setKycToModel(ShopCardViewModel shopCard, AccountModel accountModel) {
        if (shopCard != null && accountModel != null && accountModel.getKycStatusPojo() != null) {

            if (accountModel.getKycStatusPojo().getKycStatusDetailPojo() != null
                    && accountModel.getKycStatusPojo()
                    .getKycStatusDetailPojo().getIsSuccess() == KYCConstant.IS_SUCCESS_GET_STATUS) {
                shopCard.setVerificationStatus(accountModel.getKycStatusPojo()
                        .getKycStatusDetailPojo().getStatus());
                shopCard.setVerificationStatusName(accountModel.getKycStatusPojo()
                        .getKycStatusDetailPojo().getStatusName());
            } else {
                shopCard.setVerificationStatus(KYCConstant.STATUS_ERROR);
                shopCard.setVerificationStatusName("");
            }
        }
    }

    private SellerViewModel getEmptySellerModel() {
        SellerViewModel sellerViewModel = new SellerViewModel();
        List<ParcelableViewModel> items = new ArrayList<>();

        items.add(new SellerEmptyViewModel());
        sellerViewModel.setItems(items);

        return sellerViewModel;
    }

    private ShopCardViewModel getShopInfoMenu(AccountModel accountModel, DataDeposit dataDeposit) {
        ShopCardViewModel shopCard = new ShopCardViewModel();
        shopCard.setShopImageUrl(accountModel.getShopInfo().getInfo().getShopId());
        shopCard.setShopId(accountModel.getShopInfo().getInfo().getShopId());
        shopCard.setShopName(accountModel.getShopInfo().getInfo().getShopName());
        shopCard.setShopImageUrl(accountModel.getShopInfo().getInfo().getShopAvatar());
        shopCard.setGoldMerchant(accountModel.getShopInfo().getOwner().getGoldMerchant());
        shopCard.setDataDeposit(dataDeposit);

        if (accountModel.getReputationShops() != null && accountModel.getReputationShops().size() > 0) {
            shopCard.setReputationImageUrl(accountModel.getReputationShops().get(0).getBadgeHd());
        }

        setKycToModel(shopCard, accountModel);

        return shopCard;
    }

    private SellerSaldoViewModel getSaldoInfo(DepositModel depositModel) {
        SellerSaldoViewModel sellerSaldoCard = new SellerSaldoViewModel();
        if (depositModel.getDepositLong() != 0) {
            sellerSaldoCard.setBalance(CurrencyFormatUtil.convertPriceValueToIdrFormat
                    (depositModel.getDepositLong(), false));
        }
        return sellerSaldoCard;
    }

    private List<MenuGridItemViewModel> getSellerOrderMenu(Boolean isNotNull, AccountModel accountModel) {
        List<MenuGridItemViewModel> menuGridItems = new ArrayList<>();
        MenuGridItemViewModel gridItem = new MenuGridItemViewModel(
                R.drawable.ic_new_order,
                context.getString(R.string.label_menu_new_order),
                ApplinkConst.SELLER_NEW_ORDER,
                isNotNull ? accountModel.getNotifications().getSellerOrder().getNewOrder() : 0,
                PENJUAL,
                context.getString(R.string.title_menu_sales)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_ready_to_ship,
                context.getString(R.string.label_menu_ready_to_ship),
                ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP,
                isNotNull ? accountModel.getNotifications().getSellerOrder().getReadyToShip() : 0,
                PENJUAL,
                context.getString(R.string.title_menu_sales)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_shipped,
                context.getString(R.string.label_menu_shipped),
                ApplinkConst.SELLER_PURCHASE_SHIPPED,
                isNotNull ? accountModel.getNotifications().getSellerOrder().getShipped() : 0,
                PENJUAL,
                context.getString(R.string.title_menu_sales)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_delivered,
                context.getString(R.string.label_menu_arrive_at_destination),
                ApplinkConst.SELLER_PURCHASE_DELIVERED,
                isNotNull ? accountModel.getNotifications().getSellerOrder().getArriveAtDestination() : 0,
                PENJUAL,
                context.getString(R.string.title_menu_sales)
        );
        menuGridItems.add(gridItem);

        return menuGridItems;
    }

    private ParcelableViewModel getSellerResolutionMenu(AccountModel accountModel) {
        MenuListViewModel menuList = new MenuListViewModel();
        menuList.setMenu(context.getString(R.string.title_menu_seller_complain));
        menuList.setMenuDescription(context.getString(R.string.label_menu_seller_complain));
        if (accountModel.getNotifications() != null
                && accountModel.getNotifications().getResolution() != null) {
            menuList.setCount(accountModel.getNotifications().getResolution().getSeller());
        }
        menuList.setApplink(ApplinkConst.RESCENTER_SELLER);
        menuList.setTitleTrack(PENJUAL);
        menuList.setSectionTrack(context.getString(R.string.title_menu_sales));

        return menuList;
    }
}
