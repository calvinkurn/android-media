package com.tokopedia.home.account.presentation.view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.AccountHomeUrl;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.presentation.adapter.MenuGridAdapter;
import com.tokopedia.home.account.presentation.listener.AccountItemListener;
import com.tokopedia.home.account.presentation.util.MenuGridSpacingDecoration;
import com.tokopedia.home.account.presentation.viewmodel.MenuGridItemViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.home.account.AccountConstants.Analytics.PEMBELI;
import static com.tokopedia.home.account.AccountConstants.RC_GIFTCARD_ENABLE;

/**
 * @author by alvinatin on 01/08/18.
 */

public class SeeAllView extends BottomSheets {

    private AccountItemListener listener;
    private static final int COLUMN_COUNT = 4;

    @Override
    public int getLayoutResourceId() {
        return R.layout.view_see_all;
    }

    @Override
    protected BottomSheetsState state() {
        return BottomSheetsState.FULL;
    }

    @Override
    public void initView(View view) {
        RecyclerView holder = view.findViewById(R.id.holder_all_item);

        List<MenuGridItemViewModel> list = createItems();

        MenuGridAdapter adapter = new MenuGridAdapter(listener);
        holder.setAdapter(adapter);
        holder.setLayoutManager(
                new GridLayoutManager(view.getContext(),
                        COLUMN_COUNT,
                        LinearLayoutManager.VERTICAL,
                        false));
        holder.addItemDecoration(
                new MenuGridSpacingDecoration(4, 0, 16, true));

        adapter.setNewData(list);
    }

    @Override
    protected String title() {
        return getString(R.string.title_menu_other_transaction);
    }

    public void setListener(AccountItemListener listener) {
        this.listener = listener;
    }

    private List<MenuGridItemViewModel> createItems() {
        List<MenuGridItemViewModel> list = new ArrayList<>();
        MenuGridItemViewModel gridItem = new MenuGridItemViewModel(
                R.drawable.ic_top_up_bill,
                getContext().getString(R.string.title_menu_top_up_bill),
                ApplinkConst.DIGITAL_ORDER,
                0,
                PEMBELI,
                getContext().getString(R.string.title_menu_transaction)
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_flight,
                getContext().getString(R.string.title_menu_flight),
                ApplinkConst.FLIGHT_ORDER,
                0,
                PEMBELI,
                getContext().getString(R.string.title_menu_transaction)
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_train,
                getContext().getString(R.string.title_menu_train),
                AccountConstants.Navigation.TRAIN_ORDER_LIST,
                0,
                PEMBELI,
                getContext().getString(R.string.title_menu_transaction)
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_deals,
                getContext().getString(R.string.title_menu_deals),
                ApplinkConst.DEALS_ORDER,
                0,
                PEMBELI,
                getContext().getString(R.string.title_menu_transaction)
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_event,
                getContext().getString(R.string.title_menu_event),
                ApplinkConst.EVENTS_ORDER,
                0,
                PEMBELI,
                getContext().getString(R.string.title_menu_transaction)
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_reksa_dana,
                getContext().getString(R.string.title_menu_reksadana),
                String.format("%s?url=%s",
                        ApplinkConst.WEBVIEW,
                        AccountHomeUrl.REKSA_DANA_TX_URL),
                0,
                PEMBELI,
                getContext().getString(R.string.title_menu_transaction)
        );
        list.add(gridItem);

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_emas,
                getContext().getString(R.string.title_menu_gold),
                String.format("%s?url=%s",
                        ApplinkConst.WEBVIEW,
                        AccountHomeUrl.EMAS_TX_URL),
                0,
                PEMBELI,
                getContext().getString(R.string.title_menu_transaction)
        );
        list.add(gridItem);

        if (((AccountHomeRouter) getContext().getApplicationContext()).getBooleanRemoteConfig(RC_GIFTCARD_ENABLE, false)) {
            gridItem = new MenuGridItemViewModel(
                    R.drawable.ic_giftcard,
                    getContext().getString(R.string.title_menu_gift_card),
                    String.format("%s?url=%s",
                            ApplinkConst.WEBVIEW,
                            AccountHomeUrl.GIFT_CARD_URL),
                    0,
                    PEMBELI,
                    getContext().getString(R.string.title_menu_transaction)
            );
            list.add(gridItem);
        }

        gridItem = new MenuGridItemViewModel(
                R.drawable.ic_belanja,
                getContext().getString(R.string.title_menu_market_place),
                ApplinkConst.MARKETPLACE_ORDER,
                0,
                PEMBELI,
                getContext().getString(R.string.title_menu_transaction)
        );
        list.add(gridItem);

        return list;
    }
}
