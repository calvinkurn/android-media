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
import android.support.test.filters.FlakyTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.mapper.CartMapper;
import com.tokopedia.checkout.domain.mapper.MapperUtil;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent;
import com.tokopedia.abstraction.common.di.module.TestAppModule;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.di.component.DaggerTestCartListComponent;
import com.tokopedia.checkout.view.di.component.TestCartListComponent;
import com.tokopedia.checkout.view.di.module.CartListModule;
import com.tokopedia.checkout.view.di.module.TestCartListModule;
import com.tokopedia.checkout.view.di.module.TestTrackingAnalyticsModule;
import com.tokopedia.checkout.view.di.module.TrackingAnalyticsModule;
import com.tokopedia.checkout.view.view.cartlist.CartFragment;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.entity.home.ProductItemData;
import com.tokopedia.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.feedplus.data.mapper.RecentProductMapper;
import com.tokopedia.feedplus.data.pojo.FeedQuery;
import com.tokopedia.feedplus.data.pojo.WhitelistQuery;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.feedplus.domain.usecase.GetFirstPageFeedsCloudUseCase;
import com.tokopedia.feedplus.view.adapter.FeedPlusAdapter;
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.feedplus.view.di.FeedPlusComponent;
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment;
import com.tokopedia.feedplus.view.presenter.FeedPlusPresenter;
import com.tokopedia.home.beranda.data.mapper.HomeMapper;
import com.tokopedia.home.beranda.domain.model.HomeData;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.common.di.DaggerKolComponent;
import com.tokopedia.kol.common.di.KolComponent;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.navigation.presentation.di.DaggerGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.DaggerTestGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavModule;
import com.tokopedia.navigation.presentation.di.TestGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.TestGlobalNavModule;
import com.tokopedia.navigation.presentation.fragment.InboxFragment;
import com.tokopedia.navigation.presentation.module.DaggerTestBerandaComponent;
import com.tokopedia.navigation.presentation.module.TestBerandaComponent;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.tkpd.ConsumerRouterApplication;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.rule.GuessTokopediaTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
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
import static com.tokopedia.tkpd.Utils.getField;
import static com.tokopedia.tkpd.Utils.nthChildOf;
import static com.tokopedia.tkpd.Utils.setField;
import static com.tokopedia.tkpd.Utils.withCustomConstraints;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static rx.Observable.just;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.usecase.RequestParams;

import retrofit2.Response;
import rx.Observable;


/**
 * Created by normansyahputa on 26/7/18.
 */
@RunWith(AndroidJUnit4.class)
public class MainParentActivityTest {

    private String jsons[] = {"feed_check_whitelist.json", "feed_query.json", "recent_product_views.json", "inbox_home.json"};


    @Inject
    HomePresenter homePresenter;

    GetDrawerNotificationUseCase getDrawerNotificationUseCase;

    private UserSession userSession;

    @Rule
    public GuessTokopediaTestRule<MainParentActivity> mIntentsRule = new GuessTokopediaTestRule<>(
            MainParentActivity.class, true, false, 3
    );

    private BaseAppComponent baseAppComponent;
    private TestAppModule testAppModule;

    @Before
    public void before(){
        BaseMainApplication application = (BaseMainApplication)InstrumentationRegistry.getTargetContext().getApplicationContext();
        baseAppComponent = application.reinitBaseAppComponent(testAppModule = new TestAppModule(application));

        // disable showcase
        final String showCaseTag = MainParentActivity.class.getName() + ".bottomNavigation";
        ShowCasePreference.setShown(application, showCaseTag, true);
    }

    @After
    public void after(){
        // disable showcase
        final String showCaseTag = MainParentActivity.class.getName() + ".bottomNavigation";
        ShowCasePreference.setShown(baseAppComponent.getContext(), showCaseTag, false);


        baseAppComponent = null;
        testAppModule = null;
        TestAppModule.userSession = null;
    }

    @Test
    public void test_load_cart_first_time_without_login() throws Exception{
        UserSession userSession = baseAppComponent.userSession();

        doReturn(false).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        prepareForFullSmartLockBundle();

        startEmptyActivity();

        onView(allOf(withText("Feed"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        doReturn(true).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();


        onView(allOf(withText("Inbox"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
        onView(allOf(withText("Keranjang"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        CartFragment fragment = (CartFragment) mIntentsRule.getActivity().getFragment(3);

        // reset all inbox state
        TestCartListComponent navComponent = DaggerTestCartListComponent.builder()
                .cartComponent(CartComponentInjector.newInstance(mIntentsRule.getActivity().getApplication()).getCartApiServiceComponent())
                .testCartListModule(new TestCartListModule(fragment))
                .testTrackingAnalyticsModule(new TestTrackingAnalyticsModule())
                .build();

        if(fragment != null && fragment.isVisible()){
            fragment.reInitInjector(navComponent);

            mIntentsRule.getActivity().runOnUiThread(() -> {
                fragment.renderEmptyCartData(
                        provideGetCartListUseCase(mIntentsRule.getActivity())
                );
            });
        }

        Thread.sleep(5_000);


        onView(allOf(withText("Akun"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
    }

    @Test
    public void test_load_inbox_first_time_without_login(){
        UserSession userSession = baseAppComponent.userSession();

        doReturn(false).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        prepareForFullSmartLockBundle();

        startEmptyActivity();

        onView(allOf(withText("Feed"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        doReturn(true).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();


        onView(allOf(withText("Inbox"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        // reset all inbox state
        TestGlobalNavComponent navComponent = DaggerTestGlobalNavComponent.builder()
                .baseAppComponent(baseAppComponent)
                .testGlobalNavModule(new TestGlobalNavModule())
                .build();

        getDrawerNotificationUseCase = navComponent.getGetDrawerNotificationUseCase();

        doReturn(Observable.just(provideNotificationEntity()))
                .when(getDrawerNotificationUseCase)
                .createObservable(any(RequestParams.class));

        InboxFragment fragment = (InboxFragment) mIntentsRule.getActivity().getFragment(2);

        if(fragment != null && fragment.isVisible()){
            fragment.reInitInjector(navComponent);

            mIntentsRule.getActivity().runOnUiThread(() -> {
                fragment.onResume();
            });


            // verify inbox is good enough
        }

        onView(allOf(withText("Keranjang"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
        onView(allOf(withText("Akun"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
    }

    /**
     * somehow mock user as already login, the all screen get freezes.
     */

    @FlakyTest
    public void test_load_inbox_first_time(){
        UserSession userSession = baseAppComponent.userSession();

        doReturn(true).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        prepareForFullSmartLockBundle();

        startEmptyActivity();

        onView(allOf(withText("Feed"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
        onView(allOf(withText("Inbox"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
        onView(allOf(withText("Keranjang"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
        onView(allOf(withText("Akun"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
    }

    @Test
    public void test_load_first_time_feed_plus() throws Exception {
        prepareForFullSmartLockBundle();

        startEmptyActivity();

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        onView(allOf(withText("Feed"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        KolComponent kolComponent = DaggerKolComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build();

        FeedPlusComponent component = DaggerFeedPlusComponent.builder()
                .kolComponent(kolComponent)
                .build();

        UserSession userSession = component.userSession();

        doReturn(true).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        FeedPlusFragment fragment = (FeedPlusFragment) mIntentsRule.getActivity().getFragment(1);
        if (fragment != null && fragment.isMainViewVisible()) {

            fragment.reInitInjector(component);

            fragment.resetToFirstTime();

            // Get total item of myRecyclerView
            RecyclerView recyclerView =fragment.getView().findViewById(R.id.recycler_view);
            FeedPlusAdapter adapter = (FeedPlusAdapter)recyclerView.getAdapter();
            mIntentsRule.getActivity().runOnUiThread(() -> {
                adapter.clearData();
            });

            Thread.sleep(1_000);

            // prepare the data
            GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase = mock(GetFirstPageFeedsCloudUseCase.class);

            fragment.getPresenter().setGetFirstPageFeedsCloudUseCase(getFirstPageFeedsCloudUseCase);

            doReturn(Observable.just(test3())).when(getFirstPageFeedsCloudUseCase).createObservable(any(RequestParams.class));

            onView(allOf(withId(R.id.swipe_refresh_layout), withTagValue(is((Object) "swipe_to_refresh_feed_plus")))).perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));
        }
    }

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

                doAnswer(invocation -> {
                    fragment.getPresenter().getView().setItems(test2());
                    return null;
                }).when(fragment.getPresenter()).getHomeData();

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

    private CartListData provideGetCartListUseCase(Context context){
        CartDataListResponse cartDataListResponse = CacheUtil.convertStringToModel(
                mIntentsRule.getBaseJsonFactory().convertFromAndroidResource("empty_cart.json")
                , CartDataListResponse.class);
        CartMapper cartMapper = new CartMapper(new MapperUtil());
        return cartMapper.convertToCartItemDataList(context, cartDataListResponse);
    }

    private NotificationEntity provideNotificationEntity(){
        return CacheUtil.convertStringToModel(
                mIntentsRule.getBaseJsonFactory().convertFromAndroidResource(jsons[3])
                , NotificationEntity.class);
    }

    private FeedResult test3() {
        
        FeedQuery homeData = CacheUtil.convertStringToModel(
                mIntentsRule.getBaseJsonFactory().convertFromAndroidResource(jsons[1])
                , FeedQuery.class);
        GraphqlResponse<FeedQuery> homeDataGraphqlResponse = new GraphqlResponse<>();
        homeDataGraphqlResponse.setData(homeData);

        Response<GraphqlResponse<FeedQuery>> response =
                Response.success(homeDataGraphqlResponse);

        FeedResult feedResult = just(response)
                .map(new FeedListMapper())
                .map(new FeedResultMapper(FeedResult.SOURCE_CLOUD))
                .defaultIfEmpty(null).toBlocking().first();

        ProductItemData productItemData = CacheUtil.convertStringToModel(
                mIntentsRule.getBaseJsonFactory().convertFromAndroidResource(jsons[2])
                , ProductItemData.class);

        DataResponse<ProductItemData> dataResponse
                = new DataResponse<>();
        dataResponse.setData(productItemData);

        Response<DataResponse<ProductItemData>> response1 =
                Response.success(dataResponse);

        feedResult.getFeedDomain().setRecentProduct(just(response1)
                .map(new RecentProductMapper(new Gson()))
                .defaultIfEmpty(null).toBlocking().first());

        WhitelistQuery whitelistQuery = CacheUtil.convertStringToModel(
                mIntentsRule.getBaseJsonFactory().convertFromAndroidResource(jsons[0])
                , WhitelistQuery.class);
        feedResult.getFeedDomain().setWhitelist(
                GetFirstPageFeedsCloudUseCase.getWhitelistDomain(whitelistQuery)
        );
        
        return feedResult;
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
