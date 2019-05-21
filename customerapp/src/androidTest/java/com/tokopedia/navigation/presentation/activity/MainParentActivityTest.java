package com.tokopedia.navigation.presentation.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.module.TestAppModule;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.mapper.CartMapper;
import com.tokopedia.checkout.domain.mapper.MapperUtil;
import com.tokopedia.checkout.view.di.component.CartComponentInjector;
import com.tokopedia.checkout.view.di.component.DaggerTestCartListComponent;
import com.tokopedia.checkout.view.di.component.TestCartListComponent;
import com.tokopedia.checkout.view.di.module.TestCartListModule;
import com.tokopedia.checkout.view.di.module.TestTrackingAnalyticsModule;
import com.tokopedia.checkout.view.view.cartlist.CartFragment;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.feedplus.data.pojo.FeedQuery;
import com.tokopedia.feedplus.data.pojo.WhitelistQuery;
import com.tokopedia.feedplus.view.adapter.FeedPlusAdapter;
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent;
import com.tokopedia.feedplus.view.di.FeedPlusComponent;
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment;
import com.tokopedia.home.account.analytics.domain.GetUserAttributesUseCase;
import com.tokopedia.home.account.data.mapper.AccountMapper;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.di.component.DaggerTestAccountHomeComponent;
import com.tokopedia.home.account.di.component.TestAccountHomeComponent;
import com.tokopedia.home.account.di.module.TestAccountHomeModule;
import com.tokopedia.home.account.presentation.fragment.AccountHomeFragment;
import com.tokopedia.home.account.presentation.viewmodel.base.AccountViewModel;
import com.tokopedia.home.beranda.data.mapper.HomeMapper;
import com.tokopedia.home.beranda.domain.model.HomeData;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment;
import com.tokopedia.kol.common.di.DaggerKolComponent;
import com.tokopedia.kol.common.di.KolComponent;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.navigation.presentation.di.DaggerTestGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.TestGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.TestGlobalNavModule;
import com.tokopedia.navigation.presentation.fragment.InboxFragment;
import com.tokopedia.navigation.presentation.module.DaggerTestBerandaComponent;
import com.tokopedia.navigation.presentation.module.TestBerandaComponent;
import com.tokopedia.navigation.presentation.presenter.MainParentPresenter;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.rule.GuessTokopediaTestRule;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.usecase.RequestParams;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
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
import static com.tokopedia.tkpd.Utils.withCustomConstraints;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static rx.Observable.just;


/**
 * Created by normansyahputa on 26/7/18.
 */
@RunWith(AndroidJUnit4.class)
public class MainParentActivityTest {

    private static final String Feed_TAG = "Feed";
    private static final String Inbox_TAG = "Inbox";
    private static final String Keranjang_TAG = "Keranjang";
    private static final String Akun_TAG = "Akun";
    private static final String Home_TAG = "Home";
    @Rule
    public GuessTokopediaTestRule<MainParentActivity> mIntentsRule = new GuessTokopediaTestRule<>(
            MainParentActivity.class, true, false, 3
    );
    @Inject
    HomePresenter homePresenter;

    GetDrawerNotificationUseCase getDrawerNotificationUseCase;
    private String jsons[] = {"feed_check_whitelist.json", "feed_query.json", "recent_product_views.json", "inbox_home.json"};
    private BaseAppComponent baseAppComponent;
    private TestAppModule testAppModule;

    @Before
    public void before() {
        BaseMainApplication application = (BaseMainApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        baseAppComponent = application.reinitBaseAppComponent(testAppModule = new TestAppModule(application));

        disableOnBoard(application);

        // disable showcase
        final String showCaseTag = MainParentActivity.class.getName() + ".bottomNavigation";
        ShowCasePreference.setShown(application, showCaseTag, true);
    }

    private boolean disableOnBoard(BaseMainApplication baseMainApplication) {
        SharedPreferences sharedPreferences = baseMainApplication.getSharedPreferences("LOGIN_SESSION", Context.MODE_PRIVATE);
        return sharedPreferences.edit().putBoolean("IS_FIRST_TIME_NEW_ONBOARDING", false).commit();
    }

    @After
    public void after() {
        // disable showcase
        final String showCaseTag = MainParentActivity.class.getName() + ".bottomNavigation";
        ShowCasePreference.setShown(baseAppComponent.getContext(), showCaseTag, false);


        baseAppComponent = null;
        testAppModule = null;
        TestAppModule.userSession = null;
    }

    @Test
    public void test_notification_navigation_bar() throws Exception {
        prepareForFullSmartLockBundle();

        startEmptyActivity();

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // reset all inbox state
        TestGlobalNavComponent navComponent = DaggerTestGlobalNavComponent.builder()
                .baseAppComponent(baseAppComponent)
                .testGlobalNavModule(new TestGlobalNavModule())
                .build();

        getDrawerNotificationUseCase = navComponent.getGetDrawerNotificationUseCase();

        MainParentPresenter mainParentPresenter = navComponent.mainParentPresenter();

        doReturn(Observable.just(provideNotificationEntity()))
                .when(getDrawerNotificationUseCase)
                .createObservable(any(RequestParams.class));

        mIntentsRule.getActivity().reInitInjector(
                navComponent);

        mIntentsRule.getActivity().runOnUiThread(mainParentPresenter::onResume);
    }

    @Test
    public void test_load_account_home_first_time_without_login() throws Exception {
        UserSession userSession = baseAppComponent.userSession();

        doReturn(false).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        prepareForFullSmartLockBundle();

        startEmptyActivity();

        onView(allOf(withText(Feed_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        doReturn(true).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        onView(allOf(withText(Inbox_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
        onView(allOf(withText(Keranjang_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
        onView(allOf(withText(Akun_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        AccountHomeFragment fragment = (AccountHomeFragment) mIntentsRule.getActivity().getFragment(4);

        TestAccountHomeModule testAccountHomeModule = new TestAccountHomeModule();
        TestAccountHomeComponent accountHomeComponent = DaggerTestAccountHomeComponent.builder()
                .baseAppComponent(baseAppComponent)
                .testAccountHomeModule(testAccountHomeModule)
                .build();

        accountHomeComponent.accountHomePresenter(); // call this to mock getAccountUseCase

        GetUserAttributesUseCase getAccountUseCase = testAccountHomeModule.getGetAccountUseCase();

        doReturn(Observable.just(provideAccountViewModel(mIntentsRule.getActivity())))
                .when(getAccountUseCase)
                .createObservable(any(RequestParams.class));


        if (fragment != null && fragment.isVisible()) {
            fragment.reInitInjector(accountHomeComponent);

            // TODO render moved to somewhere else
//            mIntentsRule.getActivity().runOnUiThread(() -> {
//                fragment.renderData(provideAccountViewModel(mIntentsRule.getActivity()));
//            });
        }

        Thread.sleep(2_000);

        onView(withId(R.id.pager_home_account))
                .perform(withCustomConstraints(swipeLeft(), isDisplayingAtLeast(85)));

        if (fragment != null && fragment.isVisible()) {
            // TODO render moved to somewhere else`
//            mIntentsRule.getActivity().runOnUiThread(() -> {
//                fragment.renderData(provide_account_view_model_without_shop(mIntentsRule.getActivity()));
//            });
        }
    }

    @Test
    public void test_load_cart_first_time_without_login() throws Exception {
        UserSession userSession = baseAppComponent.userSession();

        doReturn(false).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        prepareForFullSmartLockBundle();

        startEmptyActivity();

        onView(allOf(withText(Feed_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        doReturn(true).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        onView(allOf(withText(Inbox_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
        onView(allOf(withText(Keranjang_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        CartFragment fragment = (CartFragment) mIntentsRule.getActivity().getFragment(3);

        // reset all inbox state
        TestCartListComponent navComponent = DaggerTestCartListComponent.builder()
                .cartComponent(CartComponentInjector.newInstance(mIntentsRule.getActivity().getApplication()).getCartApiServiceComponent())
                .testCartListModule(new TestCartListModule(fragment))
                .testTrackingAnalyticsModule(new TestTrackingAnalyticsModule())
                .build();

        if (fragment != null && fragment.isVisible()) {
            fragment.reInitInjector(navComponent);

            mIntentsRule.getActivity().runOnUiThread(() -> {
                fragment.renderEmptyCartData(
                        provideGetCartListUseCase(mIntentsRule.getActivity())
                );
            });
        }

        Thread.sleep(5_000);

        onView(allOf(withText(Akun_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
    }

    @Test
    public void test_load_inbox_first_time_without_login() {
        UserSession userSession = baseAppComponent.userSession();

        doReturn(false).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        prepareForFullSmartLockBundle();

        startEmptyActivity();

        onView(allOf(withText(Feed_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        doReturn(true).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        onView(allOf(withText(Inbox_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
        onView(allOf(withText(Inbox_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

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

        if (fragment != null && fragment.isVisible()) {
            fragment.reInitInjector(navComponent);

            mIntentsRule.getActivity().runOnUiThread(() -> {
                fragment.onResume();
            });


            // verify inbox is good enough
        }

        onView(allOf(withText(Keranjang_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
        onView(allOf(withText(Akun_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
    }

    @Test
    public void test_load_first_time_feed_plus() throws Exception {
        UserSession userSession = baseAppComponent.userSession();

        doReturn(false).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        prepareForFullSmartLockBundle();

        startEmptyActivity();

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        onView(allOf(withText(Feed_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        KolComponent kolComponent = DaggerKolComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build();

        FeedPlusComponent component = DaggerFeedPlusComponent.builder()
                .kolComponent(kolComponent)
                .build();

        doReturn(true).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        FeedPlusFragment fragment = (FeedPlusFragment) mIntentsRule.getActivity().getFragment(1);
        if (fragment != null && fragment.isMainViewVisible()) {

            fragment.reInitInjector(component);

            fragment.resetToFirstTime();

            // Get total item of myRecyclerView
            RecyclerView recyclerView = fragment.getView().findViewById(R.id.recycler_view);
            FeedPlusAdapter adapter = (FeedPlusAdapter) recyclerView.getAdapter();
            mIntentsRule.getActivity().runOnUiThread(() -> {
                adapter.clearData();
            });

            Thread.sleep(1_000);

            // prepare the data

            onView(allOf(withId(R.id.swipe_refresh_layout), withTagValue(is((Object) "swipe_to_refresh_feed_plus")))).perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));

            // Get total item of myRecyclerView
            int itemCount = recyclerView.getAdapter().getItemCount();

            // Scroll to end of page with position
            onView(allOf(withId(R.id.recycler_view), withTagValue(is("feed_plus_list")), isCompletelyDisplayed()))
                    .perform(RecyclerViewActions.scrollToPosition(itemCount - 1));
        }
    }

    @Test
    public void testInitialUI() throws Exception {
        UserSession userSession = baseAppComponent.userSession();

        doReturn(false).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        prepareForFullSmartLockBundle();

        startEmptyActivity();

        onView(allOf(withText(Feed_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        onView(withText("Feed masih kosong")).check(matches(isDisplayed()));

        when(userSession.isLoggedIn()).thenReturn(true);

        mockAlreadyLogin(userSession);

        onView(allOf(withText(Inbox_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

    }

    @Test
    public void test_home_fragment() throws Exception {

        UserSession userSession = baseAppComponent.userSession();

        doReturn(false).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        prepareForFullSmartLockBundle();

        startEmptyActivity();

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        TestBerandaComponent berandaComponent
                = DaggerTestBerandaComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build();

        berandaComponent.inject(this);

        HomeFragment fragment = (HomeFragment) mIntentsRule.getActivity().getFragment(0);
        if (fragment != null && fragment.isMainViewVisible()) {
            fragment.reInitInjector(berandaComponent);

            when(homePresenter.hasNextPageFeed()).thenReturn(true);

            mIntentsRule.getActivity().runOnUiThread(() -> {
                fragment.clearAll();

                doReturn(fragment).when(fragment.getPresenter()).getView();

                doAnswer(invocation -> {
                    fragment.getPresenter().getView().setItems(provideHomeFragmentPageOne());
                    return null;
                }).when(fragment.getPresenter()).getHomeData();


            });

            // this must be set in order to work properly
            Thread.sleep(10_000);

            onView(withId(R.id.sw_refresh_layout)).perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));

//                // Get total item of myRecyclerView
            RecyclerView recyclerView = fragment.getView().findViewById(R.id.list);
            int itemCount = recyclerView.getAdapter().getItemCount();

            Log.d("Item count is ", String.valueOf(itemCount));
            onView(withTagValue(is((Object) "home_list")));

            // Scroll to end of page with position
            onView(allOf(withId(R.id.list), withTagValue(is("home_list")), isCompletelyDisplayed()))
                    .perform(RecyclerViewActions.scrollToPosition(itemCount - 1));

            // tap the bottom nav then check
            onView(allOf(withText(Home_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

            // this must be set in order to work properly
            Thread.sleep(1_000);

            onView(withId(R.id.viewpager_banner_category)).check(matches(isDisplayed()));
        }
    }

    private AccountViewModel provideAccountViewModel(Context context, String jsonPath) {
        AccountModel accountModel = CacheUtil.convertStringToModel(
                mIntentsRule.getBaseJsonFactory().convertFromAndroidResource(jsonPath)
                , AccountModel.class);
        return AccountMapper.from(context, accountModel);
    }

    private AccountViewModel provideAccountViewModel(Context context) {
        return provideAccountViewModel(context, "account_home.json");
    }

    private AccountViewModel provide_account_view_model_without_shop(Context context) {
        return provideAccountViewModel(context, "account_home_without_shop.json");
    }

    private CartListData provideGetCartListUseCase(Context context) {
        CartDataListResponse cartDataListResponse = CacheUtil.convertStringToModel(
                mIntentsRule.getBaseJsonFactory().convertFromAndroidResource("empty_cart.json")
                , CartDataListResponse.class);
        CartMapper cartMapper = new CartMapper(new MapperUtil());
        return cartMapper.convertToCartItemDataList(context, cartDataListResponse);
    }

    private NotificationEntity provideNotificationEntity() {
        return CacheUtil.convertStringToModel(
                mIntentsRule.getBaseJsonFactory().convertFromAndroidResource(jsons[3])
                , NotificationEntity.class);
    }

    private FeedResult provideFeedResult() {
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

        WhitelistQuery whitelistQuery = CacheUtil.convertStringToModel(
                mIntentsRule.getBaseJsonFactory().convertFromAndroidResource(jsons[0])
                , WhitelistQuery.class);

        return feedResult;
    }

    private List<Visitable> provideHomeFragmentPageOne() {
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

    private void prepareForFullSmartLockBundle() {
        Intent resultData = new Intent();
        Bundle bundle = new Bundle();

        resultData.putExtras(bundle);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(hasComponent(LoginActivity.class.getName())).respondWith(result);
    }

    private void mockAlreadyLogin(UserSession userSession) {
        doReturn(true).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();
    }

    private void startEmptyActivity() {
        mIntentsRule.launchActivity(null);
    }
}
