package com.tokopedia.tkpd.tkpdcontactus.home.view.fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.tkpd.tkpdcontactus.home.data.ContactUsArticleResponse;
import com.tokopedia.tkpd.tkpdcontactus.home.di.ContactUsComponent;
import com.tokopedia.tkpd.tkpdcontactus.home.di.DaggerContactUsComponent;
import com.tokopedia.tkpd.tkpdcontactus.home.view.customview.ArticleTextView;
import com.tokopedia.tkpd.tkpdcontactus.home.view.presenter.ContactUsHomeContract;
import com.tokopedia.tkpd.tkpdcontactus.home.view.presenter.ContactUsHomePresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContactUsHomeFragment extends BaseDaggerFragment implements ContactUsHomeContract.View, HasComponent<ContactUsComponent> {

    @Inject
    ContactUsHomePresenter presenter;
    @BindView(R2.id.linearlayout_popular_article)
    LinearLayout linearlayoutPopularArticle;

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
        return view;
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
        linearlayoutPopularArticle.addView(textView);
        addPopularArticleDivider();
    }

    @Override
    public void addPopularArticleDivider() {
        View divider = new View(getContext());
        Resources resources = getActivity().getResources();
        divider.setBackgroundColor(resources.getColor(R.color.green_300));
        ViewGroup.LayoutParams layoutParams = divider.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) resources.getDimension(R.dimen.divider_height_thin);
        linearlayoutPopularArticle.addView(divider,layoutParams);
    }


}
