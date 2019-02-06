package com.tokopedia.events.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.events.EventModuleRouter;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.data.source.EventsUrl;
import com.tokopedia.events.ScanQrCodeRouter;
import com.tokopedia.events.di.DaggerEventComponent;
import com.tokopedia.events.di.EventComponent;
import com.tokopedia.events.di.EventModule;
import com.tokopedia.events.domain.model.scanticket.ScanResponseInfo;
import com.tokopedia.events.view.contractor.EventsDetailsContract;
import com.tokopedia.events.view.presenter.EventsDetailsPresenter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.EventsAnalytics;
import com.tokopedia.events.view.utils.EventsGAConst;
import com.tokopedia.events.view.utils.FinishActivityReceiver;
import com.tokopedia.events.view.utils.ImageTextViewHolder;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EventDetailsActivity extends EventBaseActivity implements
        EventsDetailsContract.EventDetailsView {


    @BindView(R2.id.event_detail_banner)
    ImageView eventDetailBanner;
    @BindView(R2.id.tv_expandable_description)
    ExpandableTextView tvExpandableDescription;
    @BindView(R2.id.seemorebutton)
    TextView seemorebutton;
    @BindView(R2.id.seemorebutton_tnc)
    TextView seemorebuttonTnC;
    @BindView(R2.id.down_arrow)
    ImageView ivArrowSeating;
    @BindView(R2.id.tv_expandable_tnc)
    ExpandableTextView tvExpandableTermsNCondition;
    @BindView(R2.id.event_time)
    LinearLayout timeView;
    @BindView(R2.id.event_location)
    LinearLayout locationView;
    @BindView(R2.id.event_address)
    LinearLayout addressView;
    @BindView(R2.id.text_view_title)
    TextView textViewTitle;
    @BindView(R2.id.btn_book)
    View btnBook;
    @BindView(R2.id.progress_bar_layout)
    View progressBarLayout;
    @BindView(R2.id.prog_bar)
    ProgressBar progBar;
    @BindView(R2.id.main_content)
    FrameLayout mainContent;
    @BindView(R2.id.button_textview)
    TextView buttonTextView;
    @BindView(R2.id.expand_view)
    LinearLayout expandLayout;
    @BindView(R2.id.expand_view_tnc)
    LinearLayout expandTnc;
    @BindView(R2.id.down_arrow_tnc)
    ImageView ivArrowSeatingTnC;
    @BindView(R2.id.tv_event_price)
    TextView eventPrice;

    ImageTextViewHolder timeHolder;
    ImageTextViewHolder locationHolder;
    ImageTextViewHolder addressHolder;
    private Menu mMenu;


    EventsDetailsContract.EventDetailPresenter eventsDetailsPresenter;
    private FinishActivityReceiver finishReceiver = new FinishActivityReceiver(this);

    public static String FROM = "from";

    public static String EXTRA_EVENT_NAME_KEY = "event";

    public static final int FROM_HOME_OR_SEARCH = 1;

    public static final int FROM_DEEPLINK = 2;

    private EventsAnalytics eventsAnalytics;
    private static final int CODE = 1001;


    @DeepLink({EventsUrl.AppLink.EVENTS_DETAILS})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        String deepLink = extras.getString(DeepLink.URI);
        Uri.Builder uri = Uri.parse(deepLink).buildUpon();
        Intent destination = new Intent(context, EventDetailsActivity.class)
                .setData(uri.build())
                .putExtras(extras);
        destination.putExtra(EventsUrl.AppLink.EXTRA_FROM_PUSH, true);
        destination.putExtra(FROM, FROM_DEEPLINK);
        return destination;
    }

    @Override
    void initPresenter() {
        initInjector();
        mPresenter = eventComponent.getEventDetailsPresenter();
        eventsDetailsPresenter = (EventsDetailsContract.EventDetailPresenter) mPresenter;
    }

    @Override
    View getProgressBar() {
        return null;
    }


    protected void bindCustomViews() {
        timeHolder = new ImageTextViewHolder();
        locationHolder = new ImageTextViewHolder();
        addressHolder = new ImageTextViewHolder();

        ButterKnife.bind(timeHolder, timeView);
        ButterKnife.bind(locationHolder, locationView);
        ButterKnife.bind(addressHolder, addressView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeHolder = new ImageTextViewHolder();
        locationHolder = new ImageTextViewHolder();
        addressHolder = new ImageTextViewHolder();

        ButterKnife.bind(timeHolder, timeView);
        ButterKnife.bind(locationHolder, locationView);
        ButterKnife.bind(addressHolder, addressView);

        tvExpandableDescription.setInterpolator(new OvershootInterpolator());
        tvExpandableTermsNCondition.setInterpolator(new OvershootInterpolator());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EventModuleRouter.ACTION_CLOSE_ACTIVITY);
        LocalBroadcastManager.getInstance(this).registerReceiver(finishReceiver, intentFilter);

        eventsAnalytics = new EventsAnalytics(getApplicationContext());

        AppBarLayout appBarLayout = findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
                Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white, null);
                if (offset < -200) {
                    upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);

                } else {

                    upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                }
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.event_detail_activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan_ticket, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_scan_qr_code) {
            startActivityForResult(((ScanQrCodeRouter) this.getApplicationContext())
                    .gotoQrScannerPage(true), CODE);
            return true;
        }
        eventsDetailsPresenter.onClickOptionMenu(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void renderFromHome(CategoryItemsViewModel homedata) {
        toolbar.setTitle(homedata.getTitle());
        ImageHandler.loadImageCover2(eventDetailBanner, homedata.getImageApp());
        String dateRange = "";

        if (homedata.getMinStartDate() > 0) {
            if (homedata.getMinStartDate() == homedata.getMaxEndDate()) {
                dateRange = Utils.getSingletonInstance().convertEpochToString(homedata.getMinStartDate());
            } else {
                dateRange = Utils.getSingletonInstance().convertEpochToString(homedata.getMinStartDate())
                        + " - " + Utils.getSingletonInstance().convertEpochToString(homedata.getMaxEndDate());
            }
        } else {
            timeView.setVisibility(View.GONE);
        }

        setHolder(R.drawable.ic_time, dateRange, timeHolder);
        setHolder(R.drawable.ic_placeholder, homedata.getCityName(), locationHolder);
        setHolder(R.drawable.ic_skyline, homedata.getCityName(), addressHolder);
        textViewTitle.setText(homedata.getTitle());
        tvExpandableDescription.setText(Html.fromHtml(homedata.getLongRichDesc()));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        String tnc = homedata.getTnc();
        String splitArray[] = tnc.split("~");
        int flag = 1;

        StringBuilder tncBuffer = new StringBuilder();

        for (String line : splitArray) {
            if (flag == 1) {
                tncBuffer.append("<i>").append(line).append("</i>").append("<br>");
                flag = 2;
            } else {
                tncBuffer.append("<b>").append(line).append("</b>").append("<br>");
                flag = 1;
            }
        }
        tvExpandableTermsNCondition.setText(Html.fromHtml(tncBuffer.toString()));

        eventPrice.setText("Rp " + CurrencyUtil.convertToCurrencyString(homedata.getSalesPrice()));
    }

    @Override
    public void renderSeatLayout(String url) {
    }

    @Override
    public void renderSeatmap(String url) {
//        ImageHandler.loadImageCover2(imgvSeatingLayout, url);
    }

    @Override
    public void renderFromCloud(EventsDetailsViewModel data) {
        toolbar.setTitle(data.getTitle());
        ImageHandler.loadImageCover2(eventDetailBanner, data.getImageApp());

        if (data.getTimeRange() != null && data.getTimeRange().length() > 3)
            setHolder(R.drawable.ic_time, data.getTimeRange(), timeHolder);
        else
            timeView.setVisibility(View.GONE);

        setHolder(R.drawable.ic_placeholder, data.getCityName(), locationHolder);
        setHolder(R.drawable.ic_skyline, data.getAddress(), addressHolder);
        textViewTitle.setText(data.getTitle());
        if (data.getLongRichDesc() != null && !TextUtils.isEmpty(data.getLongRichDesc()))
            tvExpandableDescription.setText(Html.fromHtml(data.getLongRichDesc()));

        String tnc = data.getTnc();
        if (Utils.isNotNullOrEmpty(tnc)) {
            String splitArray[] = tnc.split("~");
            int flag = 1;

            StringBuilder tncBuffer = new StringBuilder();

            for (String line : splitArray) {
                if (flag == 1) {
                    tncBuffer.append("<i>").append(line).append("</i>").append("<br>");
                    flag = 2;
                } else {
                    tncBuffer.append("<b>").append(line).append("</b>").append("<br>");
                    flag = 1;
                }
            }
            tvExpandableTermsNCondition.setText(Html.fromHtml(tncBuffer.toString()));
        }

        eventPrice.setText("Rp " + CurrencyUtil.convertToCurrencyString(data.getSalesPrice()));
        eventsAnalytics.eventDigitalEventTracking(EventsGAConst.EVENT_PRODUCT_DETAIL_IMPRESSION, data.getTitle());
    }

    @Override
    public void setHolder(int resID, String label, ImageTextViewHolder holder) {

        holder.setImage(resID);
        holder.setTextView(label);

    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();
    }

    @Override
    public void hideProgressBar() {
        super.hideProgressBar();
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void setMenuItemVisibility(boolean canScanCode) {
        mMenu.findItem(R.id.action_scan_qr_code).setVisible(canScanCode);
    }

    @OnClick(R2.id.expand_view)
    void setSeemorebutton() {
        if (tvExpandableDescription.isExpanded()) {
            seemorebutton.setText(R.string.expand);
            ivArrowSeating.animate().rotation(0f);
            eventsAnalytics.eventDigitalEventTracking("deskripsi - " + getString(R.string.collapse),
                    textViewTitle.getText().toString());
        } else {
            seemorebutton.setText(R.string.collapse);
            ivArrowSeating.animate().rotation(180f);
            eventsAnalytics.eventDigitalEventTracking("deskripsi - " + getString(R.string.expand),
                    textViewTitle.getText().toString());
        }
        tvExpandableDescription.toggle();
    }

    @OnClick(R2.id.expand_view_tnc)
    void setSeemorebuttontnc() {
        if (tvExpandableTermsNCondition.isExpanded()) {
            seemorebuttonTnC.setText(R.string.expand);
            ivArrowSeatingTnC.animate().rotation(0f);
            eventsAnalytics.eventDigitalEventTracking("syarat dan ketentuan - " + getString(R.string.collapse),
                    textViewTitle.getText().toString());

        } else {
            seemorebuttonTnC.setText(R.string.collapse);
            ivArrowSeatingTnC.animate().rotation(180f);
            eventsAnalytics.eventDigitalEventTracking("syarat dan ketentuan - " + getString(R.string.expand),
                    textViewTitle.getText().toString());
        }
        tvExpandableTermsNCondition.toggle();
    }

    @OnClick(R2.id.btn_book)
    void book() {
        eventsDetailsPresenter.bookBtnClick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        eventsAnalytics.eventDigitalEventTracking( EventsGAConst.EVENT_CLICK_BACK, getScreenName());
    }

    @Override
    public String getScreenName() {
        return eventsDetailsPresenter.getSCREEN_NAME();
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Gson gson = new Gson();
        ScanResponseInfo scanResponseInfo = null;
        if (requestCode == CODE && resultCode == RESULT_OK) {
            scanResponseInfo = gson.fromJson(data.getStringExtra("scanResult"), ScanResponseInfo.class);
            Intent intent = new Intent(this, ScanQRCodeActivity.class);
            intent.putExtra("scanUrl", scanResponseInfo.getUrl());
            startActivity(intent);
        }

    }
}
