package com.tokopedia.core.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.customadapter.ListViewPeopleTransactionSummary;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.var.NotificationVariable;
import com.tokopedia.core.var.TkpdCache;

import java.util.ArrayList;

@Deprecated
public class FragmentPeopleTxCenter extends Fragment {

    public static int PEOPLE_CONFIRM = 1;
    public static int PEOPLE_VERIFICATION = 2;
    public static int PEOPLE_STATUS = 3;
    public static int PEOPLE_CANCEL = 4;
    public static int PEOPLE_ACCEPT = 5;
    public static String SHOP = "shop";

    private String state = "";

    public interface OnCenterMenuClickListener {

        /**
         *
         * @param position
         */
        void OnMenuClick(int position);
    }

    public FragmentPeopleTxCenter() {
    }

    private RefreshHandler Refresh;
    private View mainView;
    private View headerLV;
    private ListView TitleMenuListView;
    private ListViewPeopleTransactionSummary ListViewPeopleTransactionSummaryAdapter;
    private OnCenterMenuClickListener listener;
    private LocalCacheHandler cache;
    private NotificationVariable notif;
    private Activity context;

    ArrayList<String> MenuName = new ArrayList<>();
    ArrayList<String> MenuDesc = new ArrayList<>();
    ArrayList<Integer> MenuCount = new ArrayList<>();


    public static FragmentPeopleTxCenter createInstance(String type) {
        FragmentPeopleTxCenter fragment = new FragmentPeopleTxCenter();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity();
        listener = (OnCenterMenuClickListener) activity;
//        notif = MainApplication.getNotifInstance();
//        notif.setContext(activity);
//        notif.getNotifV4();
//        notif.SetOnNotifRefresh(new OnNotifRefreshListener() {
//
//            @Override
//            public void OnNotifRefresh() {
//                MenuCount.clear();
//                ListViewPeopleTransactionSummaryAdapter.notifyDataSetChanged();
//                LoadData();
//            }
//
//            @Override
//            public void OnNotifRefreshStart() {
//
//            }
//        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getString("type").equals("people")) {
            Condition1();
        } else {
            Condition2();
        }
        checkValidationToSendGoogleAnalytic(getUserVisibleHint());
        cache = new LocalCacheHandler(getActivity(), TkpdCache.NOTIFICATION_DATA);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        checkValidationToSendGoogleAnalytic(isVisibleToUser);
        if (isAdded()) {
            try {
                setLocalyticFlow();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            LoadData();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void checkValidationToSendGoogleAnalytic(boolean isVisibleToUser) {
        if (isVisibleToUser && getActivity() != null) {
            sendToGoogleAnalytic();
        }
    }

    private void sendToGoogleAnalytic() {
        if (getArguments().getString("type").equals("people")) {
//            AnalyticsHandler.init(getActivity()).
//                    type(Type.GA).sendScreen(AppScreen.SCREEN_TX_PEOPLE_CENTER);
        } else {
//            AnalyticsHandler.init(getActivity()).
//                    type(Type.GA).sendScreen(AppScreen.SCREEN_TX_SHOP_CENTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_people_tx_center, container, false);
        try {
            Refresh = new RefreshHandler(context, mainView, refreshListener());
            Refresh.setPullEnabled(false);
            TitleMenuListView = (ListView) mainView.findViewById(R.id.menu_list);
            ListViewPeopleTransactionSummaryAdapter = new ListViewPeopleTransactionSummary(getActivity(), MenuName, MenuCount, MenuDesc);
            TitleMenuListView.setAdapter(ListViewPeopleTransactionSummaryAdapter);
            TitleMenuListView.setOnItemClickListener(gridListener());

        } catch (Exception e) {
            e.printStackTrace();
        }
        LoadData();
        return mainView;
    }

    private OnItemClickListener gridListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (!getArguments().getString("type").equals("people")) {
                    if (position == 0) {
                        listener.OnMenuClick(position + 1);
                    } else if (position == 2) {
                        listener.OnMenuClick(3);
                    } else {
                        listener.OnMenuClick(2);
                    }
                } else {
                    switch (position) {
                        case 0:
                            listener.OnMenuClick(PEOPLE_CONFIRM);
                            break;
                        case 1:
                            listener.OnMenuClick(PEOPLE_VERIFICATION);
                            break;
                        case 2:
                            listener.OnMenuClick(PEOPLE_STATUS);
                            break;
                        case 3:
                            listener.OnMenuClick(PEOPLE_CANCEL);
                            break;
                        case 4:
                            listener.OnMenuClick(PEOPLE_ACCEPT);
                            break;
                    }
                }
            }
        };
    }

    private RefreshHandler.OnRefreshHandlerListener refreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                LoadData();
            }
        };
    }

    private void LoadData() {
        try {
            MenuCount.clear();
            if (getArguments().getString("type").equals("people")) {
                MenuName.clear();
                MenuCount.addAll(cache.getArrayListInteger(TkpdCache.Key.PURCHASE_COUNT));
                MenuName.addAll(cache.getArrayListString(TkpdCache.Key.PURCHASE));
            } else {
                MenuCount.addAll(cache.getArrayListInteger(TkpdCache.Key.SALES_COUNT));
            }
            while (MenuCount.size() < 3) {
                MenuCount.add(0);
            }
            Refresh.finishRefresh();
            ListViewPeopleTransactionSummaryAdapter.notifyDataSetChanged();
//			gvAdapter.notifyDataSetChanged();
            Refresh.setPullEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Condition1() {
        MenuName.add(getString(R.string.payment_confirm));
        MenuName.add(getString(R.string.payment_verify));
        MenuName.add(getString(R.string.order_status));
        MenuName.add(getString(R.string.title_receive_confirmation_dashboard));
        MenuName.add(getString(R.string.title_transaction_list));

        MenuDesc.add(getString(R.string.payment_confirm_desc));
        MenuDesc.add(getString(R.string.payment_verification_desc));
        MenuDesc.add(getString(R.string.order_status_desc));
        MenuDesc.add(getString(R.string.title_receive_confirmation_dashboard_desc));
        MenuDesc.add(getString(R.string.title_transaction_list_desc));

        state = "people";
    }

    private void Condition2() {
        MenuName.add(getString(R.string.title_new_order));
        MenuName.add(getString(R.string.shipping_confirm));
        MenuName.add(getString(R.string.shipping_status));

        MenuDesc.add(getString(R.string.title_new_order_desc));
        MenuDesc.add(getString(R.string.shipping_confirm_desc));
        MenuDesc.add(getString(R.string.shipping_status_desc));

        state = "shop";
    }

    private void setLocalyticFlow() {
        String screenName = getString(R.string.transaction_buy_page);

        switch (state) {
            case "people":
                CommonUtils.dumper("LocalTag : transBuyPage");
                screenName = getString(R.string.transaction_buy_page);
                break;
            case "shop":
                CommonUtils.dumper("LocalTag : transSellPage");
                screenName = getString(R.string.transaction_sell_page);
                break;
        }

        ScreenTracking.screenLoca(screenName);
        ScreenTracking.screen(screenName);
    }
}