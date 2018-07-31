package com.tokopedia.navigation.presentation.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.beranda.data.mapper.HomeMapper;
import com.tokopedia.home.beranda.domain.model.HomeData;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment;
import com.tokopedia.navigation.presentation.module.DaggerTestBerandaComponent;
import com.tokopedia.navigation.presentation.module.TestBerandaComponent;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.tkpd.ConsumerRouterApplication;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.rule.GuessTokopediaTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.openLinkWithText;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tokopedia.tkpd.Utils.nthChildOf;
import static com.tokopedia.tkpd.Utils.setField;
import static com.tokopedia.tkpd.Utils.withCustomConstraints;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static rx.Observable.just;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;

import retrofit2.Response;


/**
 * Created by normansyahputa on 26/7/18.
 */
@RunWith(AndroidJUnit4.class)
public class MainParentActivityTest {

    @Inject
    HomePresenter homePresenter;

    private UserSession userSession;

    @Rule
    public GuessTokopediaTestRule<MainParentActivity> mIntentsRule = new GuessTokopediaTestRule<>(
            MainParentActivity.class, true, false, 3
    );

    /**
     *
     * @throws Exception
     */
    @Test
    public void testInitialUI() throws Exception {
        prepareForFullSmartLockBundle();

        startEmptyActivity();

        onView(allOf(withText("Feed"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        onView(withText("Feed masih kosong")).check(matches(isDisplayed()));

        when(userSession.isLoggedIn()).thenReturn(true);

        mockAlreadyLogin3();

        onView(allOf(withText("Inbox"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        // TODO tap all nav bar button
        // TODO verify go to all locations
        // TODO tap toolbar then go back
        // TODO check checked nav bar
    }

    @Test
    public void testHomeFragment() throws Exception {
        prepareForFullSmartLockBundle();

        startEmptyActivity();

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        TestBerandaComponent berandaComponent
                = DaggerTestBerandaComponent.builder()
                .baseAppComponent(((BaseMainApplication) mIntentsRule.getActivity().getApplication()).getBaseAppComponent())
                .build();

        berandaComponent.inject(this);

        HomeFragment fragment = (HomeFragment) mIntentsRule.getActivity().getFragment(0);
        if (fragment != null && fragment.isMainViewVisible()) {
            fragment.reInitInjector(berandaComponent);

            when(homePresenter.hasNextPageFeed()).thenReturn(true);

            Thread.sleep(5_000);

            mIntentsRule.getActivity().runOnUiThread(() -> {
                fragment.clearAll();

                doReturn(fragment).when(fragment.getPresenter()).getView();

//                doAnswer(invocation -> {
//                    fragment.getPresenter().getView().setItems(new ArrayList<Visitable>() {
//                        {
//                            add(test());
//                        }
//                    });
//                    return null;
//                }).when(fragment.getPresenter()).fetchNextPageFeed();

                doAnswer(invocation -> {
                    fragment.getPresenter().getView().setItems(test2());
                    return null;
                }).when(fragment.getPresenter()).getHomeData();

//                fragment.getPresenter().fetchNextPageFeed();

                onView(withId(R.id.sw_refresh_layout)).perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));
            });
        }
    }

    @Test
    public void testHomeFragment2() throws Exception {
        prepareForFullSmartLockBundle();

        startEmptyActivity();

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        HomeFragment fragment = (HomeFragment) mIntentsRule.getActivity().getFragment(0);
        if (fragment != null && fragment.isMainViewVisible()) {
            homePresenter = spy(fragment.getPresenter());
            fragment.setPresenter(homePresenter);

            when(homePresenter.hasNextPageFeed()).thenReturn(true);

            Thread.sleep(5_000);

            mIntentsRule.getActivity().runOnUiThread(() -> {
                fragment.clearAll();

                doAnswer(invocation -> {
                    homePresenter.getView().setItems(test2());
                    return null;
                }).when(homePresenter).getHomeData();

            });

            Thread.sleep(1_000);

            onView(withId(R.id.sw_refresh_layout)).perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));

            // Get total item of myRecyclerView
            RecyclerView recyclerView =fragment.getView().findViewById(R.id.list);
            int itemCount = recyclerView.getAdapter().getItemCount();

            Log.d("Item count is ", String.valueOf(itemCount));
            onView(withTagValue(is((Object) "home_list")));

            // Scroll to end of page with position
            onView(allOf(withId(R.id.list), withTagValue(is("home_list")), isCompletelyDisplayed()))
                    .perform(RecyclerViewActions.scrollToPosition(itemCount - 1));
        }
    }

    private List<Visitable> test2() {
        HomeData homeData = CacheUtil.convertStringToModel(
                mIntentsRule.getBaseJsonFactory().convertFromAndroidResource("home_header.json")
                , HomeData.class);
        GraphqlResponse<HomeData> homeDataGraphqlResponse = new GraphqlResponse<>();
        homeDataGraphqlResponse.setData(homeData);

        Response<GraphqlResponse<HomeData>> response =
                Response.success(homeDataGraphqlResponse);

        return just(response)
                .map(new HomeMapper())
                .defaultIfEmpty(null).toBlocking().first();

    }

    private BannerViewModel test(){
        BannerViewModel bannerViewModel = new BannerViewModel();


        List<BannerSlidesModel> slides = new ArrayList<>();
        BannerSlidesModel bannerSlidesModel = new BannerSlidesModel();
        bannerSlidesModel.setApplink("tokopedia://promo/digital-cashback-11");
        bannerSlidesModel.setId(47);
        bannerSlidesModel.setImageUrl("https://ecs7.tokopedia.net/img/cache/1242/banner/2018/7/20/25618007/25618007_3e48f2b4-3d81-44c9-870b-7782b14e6a6b.jpg");
        bannerSlidesModel.setRedirectUrl("tokopedia://promo/digital-cashback-11");
        bannerSlidesModel.setTitle("DG_SENINTRAVEL_CB300K");

        BannerSlidesModel bannerSlidesModel1 = new BannerSlidesModel();
        bannerSlidesModel1.setApplink("tokopedia://promo/digital-cashback-11");
        bannerSlidesModel1.setId(47);
        bannerSlidesModel1.setImageUrl("https://ecs7.tokopedia.net/img/cache/1242/banner/2018/7/20/25618007/25618007_3e48f2b4-3d81-44c9-870b-7782b14e6a6b.jpg");
        bannerSlidesModel1.setRedirectUrl("tokopedia://promo/digital-cashback-11");
        bannerSlidesModel1.setTitle("DG_SENINTRAVEL_CB300K");
        slides.add(bannerSlidesModel);
        slides.add(bannerSlidesModel1);

        bannerViewModel.setSlides(slides);
        return bannerViewModel;
    }

    @Test
    public void testSluggishTap(){
        // TODO iterate 1000 times, and randomly tap navbar
    }

    @Test
    public void testSearchGoToDiscovery(){

    }

    @Test
    public void testTapGoToBarCode(){

    }

    private void prepareForFullSmartLockBundle() {
        Intent resultData = new Intent();
        Bundle bundle = new Bundle();
        String phoneNumber = "123-345-6789";
        bundle.putString("phone", phoneNumber);
        bundle.putString("username", "cincin.jati+47@tokopedia.com");
        bundle.putString("password", "optimus");

        resultData.putExtras(bundle);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(hasComponent(LoginActivity.class.getName())).respondWith(result);
    }

    private void mockAlreadyLogin2(){
        mIntentsRule.getActivity().setUserSession(userSession);
    }

    private void mockAlreadyLogin3(){
        setField(mIntentsRule.getActivity().getClass(), mIntentsRule.getActivity(), "userSession", userSession);
    }

    private void mockAlreadyLogin(){
        Context applicationContext = InstrumentationRegistry.getTargetContext().getApplicationContext();
        if(applicationContext != null && applicationContext instanceof ConsumerRouterApplication){
            ConsumerRouterApplication routerApplication = (ConsumerRouterApplication) applicationContext;

            setField(ConsumerRouterApplication.class, routerApplication, "userSession", userSession);

        }
    }

    private Matcher<View> bottomNavViewMatcher(@IdRes final int id_) {
        return allOf(nthChildOf(
                withId(R.id.bottomnav),
                0), withId(id_));
    }

    private ViewInteraction bottomNavViewTextMatcher(Matcher<View> parent) {
        return onView(nthChildOf(nthChildOf(parent, 2), 0 ));
    }

    private void startEmptyActivity() {
        userSession = mock(UserSession.class);

        mIntentsRule.launchActivity(null);
    }
}
