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
import com.tokopedia.home.account.presentation.viewmodel.ShopCardViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;
import com.tokopedia.navigation_common.model.NotificationResolutionModel;
import com.tokopedia.navigation_common.model.NotificationSellerOrderModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

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
        SellerViewModel sellerViewModel;
        if (accountModel.getShopInfo() != null
                && accountModel.getShopInfo().getInfo() != null
                && !TextUtils.isEmpty(accountModel.getShopInfo().getInfo().getShopId())
                && !accountModel.getShopInfo().getInfo().getShopId().equalsIgnoreCase("-1")) {
            sellerViewModel = getSellerModel(context, accountModel);
            sellerViewModel.setSeller(true);
        } else {
            sellerViewModel = getEmptySellerModel();
            sellerViewModel.setSeller(false);
        }

        return sellerViewModel;
    }

    private SellerViewModel getSellerModel(Context context, AccountModel accountModel) {
        SellerViewModel sellerViewModel = new SellerViewModel();
        List<ParcelableViewModel> items = new ArrayList<>();

        if(accountModel.getShopInfo() != null && accountModel.getShopInfo().getInfo() != null) {
            items.add(getShopInfoMenu(accountModel));
        }

        MenuGridViewModel menuGrid = new MenuGridViewModel();
        menuGrid.setTitle(context.getString(R.string.title_menu_sales));
        menuGrid.setLinkText(context.getString(R.string.label_menu_show_history));
        menuGrid.setApplinkUrl(ApplinkConst.SELLER_HISTORY);
        menuGrid.setTitleTrack(PENJUAL);
        menuGrid.setSectionTrack(context.getString(R.string.title_menu_sales));

        if(accountModel.getNotifications() != null
                && accountModel.getNotifications().getSellerOrder() != null) {
            menuGrid.setItems(getSellerOrderMenu(accountModel.getNotifications().getSellerOrder()));
        }
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

        String mitraTopperMaxLoan = "";
        if (accountModel.getLePreapprove() != null &&
                accountModel.getLePreapprove().getFieldData() != null &&
                accountModel.getLePreapprove().getFieldData().getPreApp() != null) {
            mitraTopperMaxLoan = accountModel.getLePreapprove().getFieldData().getPreApp().getPartnerMaxLoan();
        }

        try {
            Long loan = Long.parseLong(mitraTopperMaxLoan);
            if (loan > 0) {
                mitraTopperMaxLoan = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        Long.parseLong(mitraTopperMaxLoan),
                        true);
            }
        } catch (NumberFormatException e) { /*ignore*/ }

        if (!mitraTopperMaxLoan.isEmpty() && !mitraTopperMaxLoan.equals("0")) {
            InfoCardViewModel infoCardViewModel = new InfoCardViewModel();
            infoCardViewModel.setIconRes(R.drawable.ic_personal_loan);
            infoCardViewModel.setMainText(context.getString(R.string.title_menu_loan));
            infoCardViewModel.setSecondaryText(String.format("%s %s", context.getString(R.string.label_menu_loan), mitraTopperMaxLoan));
            infoCardViewModel.setApplink(AccountConstants.Navigation.MITRA_TOPPERS);
            items.add(infoCardViewModel);
        }

        sellerViewModel.setItems(items);
        return sellerViewModel;
    }

    private SellerViewModel getEmptySellerModel(){
        SellerViewModel sellerViewModel = new SellerViewModel();
        List<ParcelableViewModel> items = new ArrayList<>();

        items.add(new SellerEmptyViewModel());
        sellerViewModel.setItems(items);

        return sellerViewModel;
    }

    private ShopCardViewModel getShopInfoMenu(AccountModel accountModel) {
        ShopCardViewModel shopCard = new ShopCardViewModel();
        shopCard.setShopImageUrl(accountModel.getShopInfo().getInfo().getShopId());
        shopCard.setShopId(accountModel.getShopInfo().getInfo().getShopId());
        shopCard.setShopName(accountModel.getShopInfo().getInfo().getShopName());
        shopCard.setShopImageUrl(accountModel.getShopInfo().getInfo().getShopAvatar());
        shopCard.setGoldMerchant(accountModel.getShopInfo().getOwner().getGoldMerchant());

        if(accountModel.getDeposit() != null) {
            shopCard.setBalance(accountModel.getDeposit().getDepositFmt());
        }

        if (accountModel.getReputationShops() != null && accountModel.getReputationShops().size() > 0) {
            shopCard.setReputationImageUrl(accountModel.getReputationShops().get(0).getBadgeHd());
        }

        return shopCard;
    }

    private List<MenuGridItemViewModel> getSellerOrderMenu(NotificationSellerOrderModel sellerOrder) {
        List<MenuGridItemViewModel> menuGridItems = new ArrayList<>();
        MenuGridItemViewModel gridItem = new MenuGridItemViewModel(
                R.drawable.ic_new_order,
                context.getString(R.string.label_menu_new_order),
                ApplinkConst.SELLER_NEW_ORDER,
                sellerOrder.getNewOrder(),
                PENJUAL,
                context.getString(R.string.title_menu_sales)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_ready_to_ship,
                context.getString(R.string.label_menu_ready_to_ship),
                ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP,
                sellerOrder.getReadyToShip(),
                PENJUAL,
                context.getString(R.string.title_menu_sales)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_shipped,
                context.getString(R.string.label_menu_shipped),
                ApplinkConst.SELLER_PURCHASE_SHIPPED,
                sellerOrder.getShipped(),
                PENJUAL,
                context.getString(R.string.title_menu_sales)
        );
        menuGridItems.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_delivered,
                context.getString(R.string.label_menu_arrive_at_destination),
                ApplinkConst.SELLER_PURCHASE_DELIVERED,
                sellerOrder.getArriveAtDestination(),
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
        if(accountModel.getNotifications() != null
                && accountModel.getNotifications().getResolution() != null) {
            menuList.setCount(accountModel.getNotifications().getResolution().getSeller());
        }
        menuList.setApplink(ApplinkConst.RESCENTER_SELLER);
        menuList.setTitleTrack(PENJUAL);
        menuList.setSectionTrack(context.getString(R.string.title_menu_sales));

        return menuList;
    }
}
