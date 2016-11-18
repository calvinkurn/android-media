package com.tokopedia.core.var;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.customadapter.ListViewNotification3;
import com.tokopedia.core.drawer.interactor.NetworkInteractor;
import com.tokopedia.core.drawer.interactor.NetworkInteractorImpl;
import com.tokopedia.core.drawer.var.NotificationItem;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.inboxmessage.activity.InboxMessageActivity;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.inboxticket.activity.InboxTicketActivity;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.notification.model.Notification;
import com.tokopedia.core.rescenter.inbox.activity.InboxResCenterActivity;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.talk.inboxtalk.activity.InboxTalkActivity;
import com.tokopedia.core.util.FullListViewHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.widgets.TkpdWidgetReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Deprecated
public class NotificationVariable {

    public static final String STUART = "STUART";
    public static final String MESSAGE_TAG = "Notification Variable";

    public interface OnNotifRefreshListener {
        void OnNotifRefresh();

        void OnNotifRefreshStart();
    }

    public Activity context;
    public ListView LvNotif;
    public ListViewNotification3 LvNotifAdapter;
    public static boolean isNotificationShow = false;
    public ImageView NotifBut;
    public ArrayList<String> NotifName = new ArrayList<String>();
    public ArrayList<String> Message = new ArrayList<String>();
    public ArrayList<String> Sales = new ArrayList<String>();
    public ArrayList<String> Purchase = new ArrayList<String>();
    public ArrayList<Integer> MessageCount = new ArrayList<Integer>();
    public ArrayList<Integer> SalesCount = new ArrayList<Integer>();
    public ArrayList<Integer> PurchaseCount = new ArrayList<Integer>();
    public ArrayList<Integer> TotalAvail = new ArrayList<Integer>();
    public LocalCacheHandler Cache;
    public int IncNotif = 0;
    public int IsHasCart = 0;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private View MainView;
    private double deltaY = 0;
    private double startY = 0;
    private int FullHeight;
    private RelativeLayout.LayoutParams param;
    private Animation reOpenDrawer;
    private TextView mMessage;
    private TextView mTalk;
    private TextView mReview;
    private TextView mTicket;
    private TextView mResCenter;
    private View vMessage;
    private View vTalk;
    private View vReview;
    private View vTicket;
    private View vResCenter;
    private TextView NotifCount;
    private int TotalNotif;
    private OnNotifRefreshListener listener = null;
    Bundle bundle;

    public NotificationVariable(Activity context) {
        this.context = context;
        Cache = new LocalCacheHandler(context, TkpdCache.NOTIFICATION_DATA);
    }

    public void SetOnNotifRefresh(OnNotifRefreshListener listener) {
        this.listener = listener;
    }

    public NotificationVariable() {
    }

    public void setContext(Activity context) {
        this.context = context;
        Cache = new LocalCacheHandler(context, TkpdCache.NOTIFICATION_DATA);
    }

    public void CreateNotification() {
        context.getActionBar().setDisplayShowTitleEnabled(false);
        TotalAvail.add(4);
        TotalAvail.add(0);
        TotalAvail.add(0);
        MessageCount.add(0);
        MessageCount.add(0);
        MessageCount.add(0);
        MessageCount.add(0);
        MessageCount.add(0);
        if (SessionHandler.isV4Login(context)) {
            fragmentManager = context.getFragmentManager();
            fragment = new FragmentNotification();
            View ABCustomView = context.getLayoutInflater().inflate(
                    R.layout.custom_action_bar, null);
            NotifBut = (ImageView) ABCustomView
                    .findViewById(R.id.search_but_ab);
            NotifCount = (TextView) ABCustomView
                    .findViewById(R.id.count_notif);
            NotifBut.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    NotifCount.setBackgroundResource(R.drawable.notif_green);
                    new NotificationModHandler(context).cancelNotif();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    fragmentTransaction
                            .setCustomAnimations(R.anim.slide_down,
                                    R.anim.slide_up, R.anim.slide_down,
                                    R.anim.slide_up);
                    if (!isNotificationShow) {
                        /*
                         * System.out.println("false");
						 * NotifBut.setImageResource
						 * (R.drawable.ic_menu_notif_transaction_active); if
						 * (IncNotif >= 1) ResetNotification();
						 * overlayInfo.show();
						 */
                        if (IncNotif >= 1) {
                            ResetNotification();
                        }
                        isNotificationShow = true;
                        fragmentTransaction.replace(android.R.id.content,
                                fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        /*
                         * System.out.println("true");
						 * NotifBut.setImageResource(
						 * R.drawable.ic_new_action_notif);
						 * overlayInfo.hide();
						 */
                        isNotificationShow = false;
                        fragmentManager.popBackStack();
                        // fragmentTransaction.remove(fragment).commit();
                    }
                }

            });
            context.getActionBar().setDisplayOptions(
                    ActionBar.DISPLAY_SHOW_CUSTOM,
                    ActionBar.DISPLAY_SHOW_CUSTOM);
            context.getActionBar().setCustomView(ABCustomView);
            NotifName.add(context.getString(R.string.title_inbox));
            NotifName.add(context.getString(R.string.title_sales));
            NotifName.add(context.getString(R.string.title_purchase));
            if (Cache.getArrayListString(TkpdCache.Key.MESSAGE).size() == 0) {
                GetNotif();
            } else if (Cache.getArrayListString(TkpdCache.Key.MESSAGE).size() < 5) {
                GetNotif();
            } else if (Cache.getInt(TkpdCache.Key.TOTAL_NOTIF) == -1) {
                GetNotif();
            } else {
                Message = Cache.getArrayListString(TkpdCache.Key.MESSAGE);
                MessageCount.clear();
                MessageCount = Cache.getArrayListInteger(TkpdCache.Key.MESSAGE_COUNT);
                Sales = Cache.getArrayListString(TkpdCache.Key.SALES);
                SalesCount = Cache.getArrayListInteger(TkpdCache.Key.SALES_COUNT);
                Purchase = Cache.getArrayListString(TkpdCache.Key.PURCHASE);
                PurchaseCount = Cache.getArrayListInteger(TkpdCache.Key.PURCHASE_COUNT);
                TotalAvail.clear();
                TotalAvail = Cache.getArrayListInteger(TkpdCache.Key.TOTAL_AVAIL);
                IncNotif = Cache.getInt(TkpdCache.Key.INC_NOTIF, 0);
                IsHasCart = Cache.getInt(TkpdCache.Key.IS_HAS_CART);
                CommonUtils.dumper("inc notifnya: " + MessageCount);
                if (IncNotif >= 1) {
                    NotifCount.setBackgroundResource(R.drawable.notif_red);
                    //NotifBut.setImageResource(R.drawable.ic_btn_notif_sign);
                } else {
                    NotifCount.setBackgroundResource(R.drawable.notif_green);
                }
                setNotification();
                GetNotif();
            }

            TotalNotif = Cache.getInt(TkpdCache.Key.TOTAL_NOTIF, 0);
            if (TotalNotif <= 0) {
                NotifCount.setVisibility(View.GONE);
            } else {
                NotifCount.setVisibility(View.VISIBLE);
                NotifCount.setText(Integer.toString(TotalNotif));
            }
        }


    }

    public void getNotifV4() {
        try {
            if (listener != null)
                listener.OnNotifRefreshStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!SessionHandler.isV2Login(context) || !SessionHandler.isV4Login(context)) {
            return;
        }

        new NotificationService().getApi().getNotification(
                AuthUtil.generateParams(context, new HashMap<String, String>())
        ).subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                TkpdResponse response = responseData.body();

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

                                    Gson gson = new GsonBuilder().create();
                                    Notification.Data data =
                                            gson.fromJson(jsonObject.toString(), Notification.Data.class);

                                    parseNotif(data);

                                } catch (JSONException je) {
                                    Log.e(STUART, MESSAGE_TAG + je.getLocalizedMessage());
                                }
                            }
                        }
                );
    }

//	private int parseToInt(String message){
//		if(message ==null)
//
//	}

    private void parseNotif(Notification.Data data) {
        Message.clear();
        MessageCount.clear();
        Sales.clear();
        SalesCount.clear();
        Purchase.clear();
        PurchaseCount.clear();
        TotalAvail.clear();

        try {

            if (data.getSales() != null) {
                Sales.add(context.getString(R.string.new_order));
                SalesCount.add(Integer.parseInt(data.getSales().getSales_new_order()));
                Sales.add(context.getString(R.string.shipping_confirm));
                SalesCount.add(Integer.parseInt(data.getSales().getSales_shipping_confirm()));
                Sales.add(context.getString(R.string.shipping_status));
                SalesCount.add(Integer.parseInt(data.getSales().getSales_shipping_status()));
            }

            Purchase.add(context.getString(R.string.payment_confirm));
            PurchaseCount.add(Integer.parseInt(data.getPurchase().getPurchase_payment_conf()));
            Purchase.add(context.getString(R.string.payment_verify));
            PurchaseCount.add(Integer.parseInt(data.getPurchase().getPurchase_payment_confirm()));
            Purchase.add(context.getString(R.string.order_status));
            PurchaseCount.add(Integer.parseInt(data.getPurchase().getPurchase_order_status()));
            Purchase.add(context.getString(R.string.delivery_confirm));
            PurchaseCount.add(Integer.parseInt(data.getPurchase().getPurchase_delivery_confirm()));
            Purchase.add(context.getString(R.string.reorder));
            PurchaseCount.add(Integer.parseInt(data.getPurchase().getPurchase_reorder()));

            Message.add(context.getString(R.string.message));
            MessageCount.add(Integer.parseInt(data.getInbox().getInbox_message()));
            Message.add(context.getString(R.string.talk));
            MessageCount.add(Integer.parseInt(data.getInbox().getInbox_talk()));
            Message.add(context.getString(R.string.review));
            MessageCount.add(Integer.parseInt(data.getInbox().getInbox_reputation()));
            Message.add(context.getString(R.string.title_customer_care));
            MessageCount.add(Integer.parseInt(data.getInbox().getInbox_ticket()));
            Message.add(context.getString(R.string.title_price_alert));
            MessageCount.add(Integer.parseInt(data.getInbox().getInbox_wishlist()));
            Message.add(context.getString(R.string.title_res_center));
            MessageCount.add(Integer.parseInt(data.getInbox().getInbox_ticket()));

            if (data.getIncr_notif() != null) {
                IncNotif = Integer.parseInt(data.getIncr_notif());
            } else {
                IncNotif = 0;
            }

            if (Integer.parseInt(data.getTotal_cart()) > 0) {
                IsHasCart = 1;
            } else {
                IsHasCart = 0;
                CommonUtils.dumper("LocalTag : Event - No Product in Cart");
                UnifyTracking.deleteProfileAttrLoca();
            }


            if (data.getTotal_notif() != null) {
                TotalNotif = Integer.parseInt(data.getTotal_notif());
            } else {
                TotalNotif = 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Cache.putArrayListString(TkpdCache.Key.MESSAGE, Message);
        Cache.putArrayListInteger(TkpdCache.Key.MESSAGE_COUNT, MessageCount);
        Cache.putArrayListString(TkpdCache.Key.SALES, Sales);
        Cache.putArrayListInteger(TkpdCache.Key.SALES_COUNT, SalesCount);
        Cache.putArrayListString(TkpdCache.Key.PURCHASE, Purchase);
        Cache.putArrayListInteger(TkpdCache.Key.PURCHASE_COUNT, PurchaseCount);
        Cache.putArrayListInteger(TkpdCache.Key.TOTAL_AVAIL, TotalAvail);
        Cache.putLong(TkpdCache.Key.EXPIRY, System.currentTimeMillis() / 1000);
        Cache.putInt(TkpdCache.Key.INC_NOTIF, IncNotif);
        Cache.putInt(TkpdCache.Key.IS_HAS_CART, IsHasCart);
        Cache.putInt(TkpdCache.Key.TOTAL_NOTIF, TotalNotif);
        Cache.applyEditor();
        TkpdWidgetReceiver.UpdateWidget(context);
        MainApplication.resetNotificationStatus(false);
        setNotification();
        try {
            if (listener != null)
                listener.OnNotifRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TotalNotif <= 0) {
            NotifCount.setVisibility(View.GONE);
        } else {
            NotifCount.setVisibility(View.VISIBLE);
            NotifCount.setText(Integer.toString(TotalNotif));
        }

        if (IncNotif >= 1) {
            NotifCount.setBackgroundResource(R.drawable.notif_red);
            //NotifBut.setImageResource(R.drawable.ic_btn_notif_sign);
        } else {
            NotifCount.setBackgroundResource(R.drawable.notif_green);
        }
    }


    public void GetNotif() {
        try {
            if (listener != null)
                listener.OnNotifRefreshStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!SessionHandler.isV2Login(context) || !SessionHandler.isV4Login(context)) {
            return;
        }
        NetworkInteractor networkInteractor = new NetworkInteractorImpl();
        networkInteractor.getNotification(context, new NetworkInteractor.NotificationListener() {
            @Override
            public void onSuccess(NotificationItem data) {
                Message.clear();
                MessageCount.clear();
                Sales.clear();
                SalesCount.clear();
                Purchase.clear();
                PurchaseCount.clear();
                TotalAvail.clear();

                Sales.add(context.getString(R.string.new_order));
                SalesCount.add(data.new_order);
                Sales.add(context.getString(R.string.shipping_confirm));
                SalesCount.add(data.shipping_confirm);
                Sales.add(context.getString(R.string.shipping_status));
                SalesCount.add(data.shipping_status);

                Purchase.add(context.getString(R.string.payment_confirm));
                PurchaseCount.add(data.payment_conf);
                Purchase.add(context.getString(R.string.payment_verify));
                PurchaseCount.add(data.payment_confirmed);
                Purchase.add(context.getString(R.string.order_status));
                PurchaseCount.add(data.order_status);
                Purchase.add(context.getString(R.string.delivery_confirm));
                PurchaseCount.add(data.delivery_confirm);
                Purchase.add(context.getString(R.string.reorder));
                PurchaseCount.add(data.reorder);

                Message.add(context.getString(R.string.message));
                MessageCount.add(data.message);
                Message.add(context.getString(R.string.talk));
                MessageCount.add(data.talk);
                Message.add(context.getString(R.string.review));
                MessageCount.add(data.reputation);
                Message.add(context.getString(R.string.title_customer_care));
                MessageCount.add(data.ticket);
                Message.add(context.getString(R.string.title_res_center));
                MessageCount.add(data.resolution);

                IncNotif = data.inc_notif;

                if (data.total_cart > 0) {
                    IsHasCart = 1;
                } else {
                    IsHasCart = 0;
                    CommonUtils.dumper("LocalTag : Event - No Product in Cart");
                    UnifyTracking.deleteProfileAttrLoca();
                }


                TotalNotif = data.total_notif;

                Cache.putArrayListString(TkpdCache.Key.MESSAGE, Message);
                Cache.putArrayListInteger(TkpdCache.Key.MESSAGE_COUNT, MessageCount);
                Cache.putArrayListString(TkpdCache.Key.SALES, Sales);
                Cache.putArrayListInteger(TkpdCache.Key.SALES_COUNT, SalesCount);
                Cache.putArrayListString(TkpdCache.Key.PURCHASE, Purchase);
                Cache.putArrayListInteger(TkpdCache.Key.PURCHASE_COUNT, PurchaseCount);
                Cache.putArrayListInteger(TkpdCache.Key.TOTAL_AVAIL, TotalAvail);
                Cache.putLong(TkpdCache.Key.EXPIRY, System.currentTimeMillis() / 1000);
                Cache.putInt(TkpdCache.Key.INC_NOTIF, IncNotif);
                Cache.putInt(TkpdCache.Key.IS_HAS_CART, IsHasCart);
                Cache.putInt(TkpdCache.Key.TOTAL_NOTIF, TotalNotif);
                Cache.applyEditor();
                TkpdWidgetReceiver.UpdateWidget(context);
                MainApplication.resetNotificationStatus(false);
                setNotification();
                try {
                    if (listener != null)
                        listener.OnNotifRefresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (TotalNotif <= 0) {
                    NotifCount.setVisibility(View.GONE);
                } else {
                    NotifCount.setVisibility(View.VISIBLE);
                    NotifCount.setText(Integer.toString(TotalNotif));
                }

                if (IncNotif >= 1) {
                    NotifCount.setBackgroundResource(R.drawable.notif_red);
                } else {
                    NotifCount.setBackgroundResource(R.drawable.notif_green);
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public void ResetNotification() {
        NotifCount.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_green));

        NetworkInteractor networkInteractor = new NetworkInteractorImpl();
        networkInteractor.resetNotification(context, new NetworkInteractor.ResetNotificationListener() {
            @Override
            public void onSuccess() {
                Cache.putInt(TkpdCache.Key.INC_NOTIF, 0);
                Cache.applyEditor();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void setNotification() {
        int AvailPurchaseNotif = 0;
        for (int i = 0; i < PurchaseCount.size(); i++) {
            if (PurchaseCount.get(i) > 0) {
                AvailPurchaseNotif++;
            }
        }

        int AvailSalesNotif = 0;
        for (int i = 0; i < SalesCount.size(); i++) {
            if (SalesCount.get(i) > 0) {
                AvailSalesNotif++;
            }
        }
        TotalAvail.clear();
        TotalAvail.add(4);
        TotalAvail.add(AvailSalesNotif);
        TotalAvail.add(AvailPurchaseNotif);
    }

    public void CloseNotif() {
        if (isNotificationShow) {
            fragmentManager.popBackStack();
            NotifBut.setImageResource(R.drawable.ic_new_action_notif);
            isNotificationShow = false;
        }
    }

    @SuppressLint("ValidFragment")
    public class FragmentNotification extends Fragment {

        public FragmentNotification() {
            // Required empty public constructor
        }

        @SuppressWarnings("deprecation")
        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.notif_dialog, container, false);

            NotifBut.setImageResource(R.drawable.ic_new_action_notif_active);
            LvNotif = (ListView) view.findViewById(R.id.notif_dialog_list_1);
            // LvNotifAdapter = new ListViewNotification3(context, Message,
            // MessageCount, Purchase, PurchaseCount, Sales, SalesCount,
            // NotifName, TotalAvail);
            LvNotifAdapter = new ListViewNotification3(context, Purchase,
                    PurchaseCount, Sales, SalesCount, NotifName, TotalAvail);
            FullListViewHandler fullLV = new FullListViewHandler(context,
                    LvNotif, view);
            fullLV.measureListViewIgnoreCount();
            LvNotif.setAdapter(LvNotifAdapter);
            LvNotif.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int pos, long arg3) {
                    Intent intent = null;
                    switch (pos) {
                        case 1:
//                            intent = new Intent(context, ShopTransactionV2.class);
                            intent = SellerRouter.getActivitySellingTransaction(context);
                            bundle = new Bundle();
                            bundle.putInt("tab", 1);
                            intent.putExtras(bundle);
                            break;
                        case 2:
//                            intent = new Intent(context, ShopTransactionV2.class);
                            intent = SellerRouter.getActivitySellingTransaction(context);
                            bundle = new Bundle();
                            bundle.putInt("tab", 2);
                            intent.putExtras(bundle);
                            break;
                        case 3:
//                            intent = new Intent(context, ShopTransactionV2.class);
                            intent = SellerRouter.getActivitySellingTransaction(context);
                            bundle = new Bundle();
                            bundle.putInt("tab", 3);
                            intent.putExtras(bundle);
                            break;
                        case 5:
                            intent = TransactionPurchaseRouter.createIntentConfirmPayment(context);
                            break;
                        case 6:
                            intent = TransactionPurchaseRouter.createIntentTxVerification(context);
                            break;
                        case 7:
                            intent = TransactionPurchaseRouter.createIntentTxStatus(context);
                            break;
                        case 8:
                            intent = TransactionPurchaseRouter.createIntentConfirmShipping(context);
                            break;
                        case 9:
                            intent = TransactionPurchaseRouter.createIntentTxAll(context);
                            break;
                        default:
                            break;
                        // case 0://EMPTY
                        // //intent = new Intent(context, InboxReview.class);
                        // break;
                        // case 1:
                        // intent = new Intent(context, NewInboxMessage.class);
                        // break;
                        // case 2:
                        // intent = new Intent(context, NewInboxTalk.class);
                        // break;
                        // case 3:
                        // intent = new Intent(context, NewInboxReview.class);
                        // break;
                        // case 4:
                        // intent = new Intent(context, InboxTicket.class);
                        // break;
                        // case 5://EMPTY
                        // //intent = new Intent(context, InboxReview.class);
                        // break;
                        // case 6:
                        // intent = new Intent(context, ShopTransactionNew.class);
                        // bundle = new Bundle();
                        // bundle.putInt("tab", 0);
                        // intent.putExtras(bundle);
                        // break;
                        // case 7:
                        // intent = new Intent(context, ShopTransactionNew.class);
                        // bundle = new Bundle();
                        // bundle.putInt("tab", 1);
                        // intent.putExtras(bundle);
                        // break;
                        // case 8:
                        // intent = new Intent(context, ShopTransactionNew.class);
                        // bundle = new Bundle();
                        // bundle.putInt("tab", 2);
                        // intent.putExtras(bundle);
                        // break;
                        // case 9:// EMPTY
                        // // intent = new Intent(context, InboxReview.class);
                        // break;
                        // case 10:
                        // intent = new Intent(context, PeopleTransactionNew.class);
                        // break;
                        // case 11:
                        // intent = new Intent(context, PeopleTransactionNew.class);
                        // bundle = new Bundle();
                        // bundle.putInt("tab", 1);
                        // intent.putExtras(bundle);
                        // break;
                    }
                    if (intent != null)
                        context.startActivity(intent);
                    CloseNotif();
                }
            });
            MainView = view.findViewById(R.id.main_view);
            MainView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OnGlobalLayoutListener() {

                        @SuppressLint("NewApi")
                        @Override
                        public void onGlobalLayout() {
                            param = (RelativeLayout.LayoutParams) MainView
                                    .getLayoutParams();
                            FullHeight = MainView.getHeight();
                            if (MainView.getViewTreeObserver().isAlive())
                                try {
                                    MainView.getViewTreeObserver()
                                            .removeOnGlobalLayoutListener(this);
                                } catch (NoSuchMethodError e) {
                                    e.printStackTrace();
                                    MainView.getViewTreeObserver()
                                            .removeGlobalOnLayoutListener(this);
                                }
                        }
                    });
            reOpenDrawer = new Animation() {

                @Override
                protected void applyTransformation(float interpolatedTime,
                                                   Transformation t) {
                    param.bottomMargin = (int) deltaY
                            - (int) (interpolatedTime * deltaY);
                    MainView.setLayoutParams(param);
                    MainView.invalidate();
                    super.applyTransformation(interpolatedTime, t);
                }

            };
            // reOpenDrawer.setAnimationListener(new AnimationListener() {
            //
            // @Override
            // public void onAnimationStart(Animation animation) {
            // }
            //
            // @Override
            // public void onAnimationRepeat(Animation animation) {
            // }
            //
            // @Override
            // public void onAnimationEnd(Animation animation) {
            // canCancel = true;
            // }
            // });
            view.findViewById(R.id.title_notif_list).setOnClickListener(null);
            reOpenDrawer.setDuration(100);
            final View mDrag = (View) view.findViewById(R.id.drag);
            final View liner = (View) view.findViewById(R.id.liner);
            mDrag.setOnTouchListener(new OnTouchListener() { // TODO Here

                @Override
                public boolean onTouch(View arg0, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            System.out.println("Down");
                            startY = event.getRawY();
                            liner.setBackgroundColor(context.getResources()
                                    .getColor(R.color.tkpd_dark_green));
                            break;
                        case MotionEvent.ACTION_MOVE:
                            deltaY = startY - event.getRawY();
                            if (deltaY < 0)
                                deltaY = 0;
                            param.bottomMargin = (int) deltaY;
                            MainView.setLayoutParams(param);
                            MainView.invalidate();
                            break;
                        case MotionEvent.ACTION_UP:
                            System.out.println("Up");
                            liner.setBackgroundColor(context.getResources()
                                    .getColor(R.color.tkpd_dark_gray));
                            if (deltaY >= (FullHeight / 3))
                                // LvNotif.startAnimation(aClose);
                                CloseNotif();
                            // else
                            LvNotif.startAnimation(reOpenDrawer);
                            break;
                    }
                    return true;
                }
            });
            // Inflate the layout for this fragment
            final HorizontalScrollView scroll = (HorizontalScrollView) view.findViewById(R.id.scroll);
            scroll.postDelayed(new Runnable() {

                @Override
                public void run() {
                    scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

                }
            }, 500);
            scroll.postDelayed(new Runnable() {

                @Override
                public void run() {
                    scroll.fullScroll(HorizontalScrollView.FOCUS_LEFT);

                }
            }, 1000);
            mMessage = (TextView) view.findViewById(R.id.messages);
            mTalk = (TextView) view.findViewById(R.id.talk);
            mReview = (TextView) view.findViewById(R.id.review);
            mTicket = (TextView) view.findViewById(R.id.ticket);
            mResCenter = (TextView) view.findViewById(R.id.rescenter);

            vMessage = (View) view.findViewById(R.id.message_wrapper);
            vTalk = (View) view.findViewById(R.id.talk_wrapper);
            vReview = (View) view.findViewById(R.id.review_wrapper);
            vTicket = (View) view.findViewById(R.id.ticket_wrapper);
            vResCenter = (View) view.findViewById(R.id.rescenter_wrapper);
            try {
                vMessage.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Bundle b = new Bundle();

                        Intent intent = new Intent(context, InboxMessageActivity.class);
                        // create smart click for inbox
                        if (MessageCount.get(0) > 0) {
                            intent.putExtra("unread", true);
                        }
                        context.startActivity(intent);
                        CloseNotif();
                    }
                });
                vTalk.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, InboxTalkActivity.class);
                        // create smart click for inbox
                        if (MessageCount.get(1) > 0) {
                            intent.putExtra("unread", true);
                        }
                        context.startActivity(intent);
                        CloseNotif();
                    }
                });
                vReview.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, InboxReputationActivity.class);
                        // create smart click for inbox
                        if (MessageCount.get(2) > 0) {
                            intent.putExtra("unread", true);
                        }
                        context.startActivity(intent);
                        CloseNotif();
                    }
                });
                vTicket.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, InboxTicketActivity.class);
                        // create smart click for inbox
                        if (MessageCount.get(3) > 0) {
                            intent.putExtra("unread", true);
                        }
                        context.startActivity(intent);
                        CloseNotif();
                    }
                });
                vResCenter.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, InboxResCenterActivity.class);
                        // create smart click for inbox
                        if (MessageCount.size() > 5 && MessageCount.get(5) > 0) {
                            intent.putExtra("unread", true);
                        }
                        context.startActivity(intent);
                        CloseNotif();
                    }
                });
//				mMessage.setText(MessageCount.get(0));
//				mTalk.setText(MessageCount.get(1));
//				mReview.setText(MessageCount.get(2));
//				mTicket.setText(MessageCount.get(3));
                if (MessageCount.size() < 6) {
                    MessageCount.add(0);
                    GetNotif();
                }

                mMessage.setText(MessageCount.get(0).toString());
                mTalk.setText(MessageCount.get(1).toString());
                mReview.setText(MessageCount.get(2).toString());
                mTicket.setText(MessageCount.get(3).toString());
                mResCenter.setText(MessageCount.get(4).toString());

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (NoSuchMethodError e) {
                if (MessageCount.get(0) > 0) {
                    vMessage.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.btn_shop));
                }
                if (MessageCount.get(1) > 0) {
                    vTalk.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.btn_shop));
                }
                if (MessageCount.get(2) > 0) {
                    vReview.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.btn_shop));
                }
                if (MessageCount.get(3) > 0) {
                    vTicket.setBackgroundDrawable(getResources().getDrawable(
                            R.drawable.btn_shop));
                }
            }
            if (TotalAvail.get(1) == 0 && TotalAvail.get(2) == 0)
                view.findViewById(R.id.no_notif).setVisibility(View.VISIBLE);
            else
                view.findViewById(R.id.no_notif).setVisibility(View.GONE);
            return view;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.app.Fragment#onDestroyView()
         */
        @Override
        public void onDestroyView() {
            // TODO Auto-generated method stub
            NotifBut.setImageResource(R.drawable.ic_new_action_notif);
            super.onDestroyView();
        }

    }
}
