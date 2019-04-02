package com.tokopedia.contactus.home.view.fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.common.analytics.ContactUsEventTracking;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.common.api.ContactUsURL;
import com.tokopedia.contactus.common.customview.ShadowTransformer;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.home.data.ContactUsArticleResponse;
import com.tokopedia.contactus.home.di.ContactUsComponent;
import com.tokopedia.contactus.home.di.DaggerContactUsComponent;
import com.tokopedia.contactus.home.view.BuyerPurchaseListActivity;
import com.tokopedia.contactus.home.view.adapter.CardPagerAdapter;
import com.tokopedia.contactus.home.view.customview.ArticleTextView;
import com.tokopedia.contactus.home.view.presenter.ContactUsHomeContract;
import com.tokopedia.contactus.home.view.presenter.ContactUsHomePresenter;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity;
import com.tokopedia.core.util.SessionHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ContactUsHomeFragment extends BaseDaggerFragment
        implements ContactUsHomeContract.View, HasComponent<ContactUsComponent> {

    @Inject
    ContactUsHomePresenter presenter;
    @BindView(R2.id.linearlayout_popular_article)
    LinearLayout linearlayoutPopularArticle;
    @BindView(R2.id.order_list)
    CardView orderList;
    @BindView(R2.id.no_orders)
    CardView noOrders;
    @BindView(R2.id.order_list_viewpager)
    ViewPager orderListViewpager;
    CardPagerAdapter cardAdapter;
    @BindView(R2.id.toped_bot)
    View btnChat;
    @BindView(R2.id.view_full_purchaselist)
    TextView btnFullPurchaseList;
    @BindView(R2.id.txt_hi_user)
    TextView txtHiUser;

    @BindView(R2.id.txt_user_info)
    TextView txtUserMessage;

    String msgId;
    @BindView(R2.id.pager_indicator)
    CirclePageIndicator pagerIndicator;

    private ContactUsComponent campaignComponent;

    private int MIN_BUYER_LIST_SIZE = 4;

    public ContactUsHomeFragment() {
        // Required empty public constructor
    }

    public static ContactUsHomeFragment newInstance() {
        ContactUsHomeFragment fragment = new ContactUsHomeFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_home, container, false);
        presenter.attachView(this);
        initInjector();
        ButterKnife.bind(this, view);
        cardAdapter = new CardPagerAdapter();
        ShadowTransformer shadowTransformer = new ShadowTransformer(orderListViewpager, cardAdapter);
        orderListViewpager.setPageTransformer(false, shadowTransformer);
        orderListViewpager.setPageMargin((int) getResources().getDimension(R.dimen.product_item_margin));
        orderListViewpager.setOffscreenPageLimit(3);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.contactus_menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_inbox) {
            ContactUsTracking.eventInboxClick();
            startActivity(new Intent(getContext(), InboxListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public ContactUsComponent getComponent() {
        if (campaignComponent == null) initInjector();
        return campaignComponent;
    }

    protected void initInjector() {
        campaignComponent = DaggerContactUsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        campaignComponent.inject(this);
    }

    @Override
    public void addPopularArticle(ContactUsArticleResponse articleResponse) {
        ArticleTextView textView = new ArticleTextView(getActivity());
        textView.setContactUsArticle(articleResponse);
        linearlayoutPopularArticle.addView(textView);
        addPopularArticleDivider();
    }

    @Override
    public void addPopularArticleDivider() {
        View divider = new View(getContext());
        Resources resources = getActivity().getResources();
        divider.setBackgroundColor(resources.getColor(R.color.grey_200));
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) resources.getDimension(R.dimen.divider_height_thin));
        linearlayoutPopularArticle.addView(divider, layoutParams);
    }

    @Override
    public void setEmptyPurchaseListHide() {
        noOrders.setVisibility(View.GONE);
    }


    @Override
    public void setPurchaseList(List<BuyerPurchaseList> buyerPurchaseLists) {
        orderList.setVisibility(View.VISIBLE);
        if (!SessionHandler.isUserHasShop(getContext()) && buyerPurchaseLists.size() <= MIN_BUYER_LIST_SIZE) {
            btnFullPurchaseList.setVisibility(View.GONE);
            cardAdapter.addData(buyerPurchaseLists);
        } else {
            cardAdapter.addData(buyerPurchaseLists.subList(0,
                    buyerPurchaseLists.size() < MIN_BUYER_LIST_SIZE ? buyerPurchaseLists.size() : MIN_BUYER_LIST_SIZE));
        }
        orderListViewpager.setAdapter(cardAdapter);
        if (buyerPurchaseLists.size() > 1)
            pagerIndicator.setViewPager(orderListViewpager);
    }

    @Override
    public void setChatBotVisible() {
        btnChat.setVisibility(View.VISIBLE);
    }

    @Override
    public void setChatBotMessageId(int msgId) {
        this.msgId = String.valueOf(msgId);
    }

    @Override
    public void setChatBotMessage(String message) {
        txtUserMessage.setText(Html.fromHtml(message));
    }

    @Override
    public void setHighMessageUserName(String userName) {
        txtHiUser.setText(String.format(getResources().getString(R.string.hai_user), userName));
    }


    @OnClick(R2.id.btn_view_more)
    public void onViewClicked() {
        ContactUsTracking.eventLihatBantuanClick();
        RouteManager.route(getContext(), ContactUsURL.ARTICLE_POPULAR_URL);
    }

    @OnClick(R2.id.view_full_purchaselist)
    public void onViewFullClicked() {
        ContactUsTracking.eventLihatTransaksiClick();
        startActivity(BuyerPurchaseListActivity.getInstance(getContext()));
    }


    @OnClick(R2.id.btn_contact_us)
    public void onBtnContactUsClicked() {
        ContactUsTracking.eventHomeHubungiKamiClick();
        String encodedUrl;
        try {
            encodedUrl = URLEncoder.encode(ContactUsURL.NAVIGATE_NEXT_URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            encodedUrl = ContactUsURL.NAVIGATE_NEXT_URL;
        }
        startActivity(((ContactUsModuleRouter) (getContext().getApplicationContext())).getWebviewActivityWithIntent(getContext(), encodedUrl, "Hubungi Kami"));
    }

    @OnClick(R2.id.btn_chat_toped)
    public void onBtnChatClicked() {
        ContactUsTracking.eventChatBotOkClick();
        startActivity(((ContactUsModuleRouter) (getContext().getApplicationContext()))
                .getChatBotIntent(getContext(), msgId));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
