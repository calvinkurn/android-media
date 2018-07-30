package com.tokopedia.navigation.presentation.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.base.adapter.Visitable;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.openLinkWithText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tokopedia.tkpd.Utils.nthChildOf;
import static com.tokopedia.tkpd.Utils.setField;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


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
    public void testHomeFragment() throws Exception{
        prepareForFullSmartLockBundle();

        startEmptyActivity();

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        TestBerandaComponent berandaComponent
                = DaggerTestBerandaComponent.builder()
                .baseAppComponent(((BaseMainApplication)mIntentsRule.getActivity().getApplication()).getBaseAppComponent())
                .build();

        berandaComponent.inject(this);

        HomeFragment fragment = (HomeFragment) mIntentsRule.getActivity().getFragment(0);
        if(fragment != null && fragment.isMainViewVisible()){
            fragment.reInitInjector(berandaComponent);

            when(homePresenter.hasNextPageFeed()).thenReturn(true);

            Thread.sleep(5_000);

            mIntentsRule.getActivity().runOnUiThread(() -> {
                fragment.clearAll();

                doReturn(fragment).when(fragment.getPresenter()).getView();

                doAnswer(invocation -> {
                    fragment.getPresenter().getView().setItems(new ArrayList<Visitable>(){
                        {
                            add(test());
                        }
                    });
                    return null;
                }).when(fragment.getPresenter()).fetchNextPageFeed();

                fragment.getPresenter().fetchNextPageFeed();
            });
        }
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
