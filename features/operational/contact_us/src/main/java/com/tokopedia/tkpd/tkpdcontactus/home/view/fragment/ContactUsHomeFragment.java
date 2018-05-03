package com.tokopedia.tkpd.tkpdcontactus.home.view.fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.inbox.inboxticket.activity.InboxTicketActivity;
import com.tokopedia.tkpd.tkpdcontactus.common.customview.ShadowTransformer;
import com.tokopedia.tkpd.tkpdcontactus.common.data.BuyerPurchaseList;
import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;
import com.tokopedia.tkpd.tkpdcontactus.home.di.ContactUsComponent;
import com.tokopedia.tkpd.tkpdcontactus.home.di.DaggerContactUsComponent;
import com.tokopedia.tkpd.tkpdcontactus.home.view.BuyerPurchaseListActivity;
import com.tokopedia.tkpd.tkpdcontactus.home.view.adapter.CardPagerAdapter;
import com.tokopedia.tkpd.tkpdcontactus.home.view.customview.ArticleTextView;
import com.tokopedia.tkpd.tkpdcontactus.home.view.presenter.ContactUsHomeContract;
import com.tokopedia.tkpd.tkpdcontactus.home.view.presenter.ContactUsHomePresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ContactUsHomeFragment extends BaseDaggerFragment implements ContactUsHomeContract.View, HasComponent<ContactUsComponent> {

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
    String topBotUrl;

    private ContactUsComponent campaignComponent;

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
        orderListViewpager.setPageMargin((int)getResources().getDimension(R.dimen.product_item_margin));
        orderListViewpager.setOffscreenPageLimit(3);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.contactus_menu_home,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_inbox) {
            startActivity(new Intent(getContext(),InboxTicketActivity.class));
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
        ArticleTextView textView = new ArticleTextView(getContext());
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
    public void setEmptyPurchaseListVisible() {
        noOrders.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEmptyPurchaseListHide() {
        noOrders.setVisibility(View.GONE);
    }


    @Override
    public void setPurchaseList(List<BuyerPurchaseList> buyerPurchaseLists) {
        orderList.setVisibility(View.VISIBLE);
        if(buyerPurchaseLists.size()<=0)
            btnFullPurchaseList.setVisibility(View.GONE);
        cardAdapter.addData(buyerPurchaseLists);
        orderListViewpager.setAdapter(cardAdapter);
    }

    @Override
    public void setChatBotVisible() {
        btnChat.setVisibility(View.VISIBLE);
    }

    @Override
    public void setChatBotButtonClick(int msgId) {
        topBotUrl = "tokopedia://topchat/"+msgId;
    }


    @OnClick(R2.id.btn_view_more)
    public void onViewClicked() {
        RouteManager.route(getContext(),"tokopedia://webview?url=https://www.tokopedia.com/bantuan/");
    }

    @OnClick(R2.id.view_full_purchaselist)
    public void onViewFullClicked() {
        startActivity(BuyerPurchaseListActivity.getInstance(getContext()));
    }


    @OnClick(R2.id.btn_contact_us)
    public void onBtnContactUsClicked() {
        RouteManager.route(getContext(),"tokopedia://webview?url=https://www.tokopedia.com/contact-us?utm_source=android&flag_app=1#step1");
    }

    @OnClick(R2.id.btn_chat_toped)
    public void onBtnChatClicked() {
        RouteManager.route(getContext(),topBotUrl);
    }


}
