package com.tokopedia.sellerapp.activities.sellerhome;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.spoon.Spoon;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.sellerapp.DaggerTestComponent;
import com.tokopedia.sellerapp.NestedScrollViewScrollToAction;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.RxJavaTestPlugins;
import com.tokopedia.sellerapp.SellerMainApplication;
import com.tokopedia.sellerapp.TestComponent;
import com.tokopedia.sellerapp.TestUtilModules;
import com.tokopedia.sellerapp.daggerModules.AppModule;
import com.tokopedia.sellerapp.daggerModules.NetworkModules;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;
import com.tokopedia.sellerapp.utils.Constants;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;

import javax.inject.Inject;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tokopedia.sellerapp.RestServiceTestHelper.getStringFromFile;
import static com.tokopedia.sellerapp.Utils.childAtPosition;
import static com.tokopedia.sellerapp.Utils.setupGCMHandler;
import static com.tokopedia.sellerapp.activities.gmstat.GMStatTest.BUYER_GRAPH_JSON;
import static com.tokopedia.sellerapp.activities.gmstat.GMStatTest.CAT_ID_JSON;
import static com.tokopedia.sellerapp.activities.gmstat.GMStatTest.POPULAR_PRODUCT_JSON;
import static com.tokopedia.sellerapp.activities.gmstat.GMStatTest.SEARCH_KEYWORD_JSON;
import static com.tokopedia.sellerapp.activities.gmstat.GMStatTest.SHOP_CATEGORY_JSON;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by normansyahputa on 9/11/16.
 *
 * TODO : somehow boommenu not working.
 */
@RunWith(AndroidJUnit4.class)
public class SellerHomeTest {
    public static final String GET_SHOP_INFO_JSON = "get_shop_info.json";
    public static final String GET_NOTIFICATION_JSON = "get_notification.json";
    public static final String GET_RESOLUTION_CENTER_JSON = "get_resolution_center.json";
    public static final String GET_DEPOSIT_JSON = "get_deposit.json";
    public static final String GET_ORDER_NEW_JSON = "get_order_new.json";
    public static final String GET_NOTIFICATION_ZERO_SALES_JSON = "get_notification_zero_sales.json";
    public static final String GET_ORDER_NEW_TWO_ITEM_JSON = "get_order_new_two_item.json";
    public static final String GET_NOTIFICATION_TWO_SALES_JSON = "get_notification_two_sales.json";
    public static final String GET_SHOP_INFO_FIRST_CREATED_JSON = "get_shop_info_first_created.json";
    public static final String GET_NOTIFICATION_EMPTY_JSON = "get_notification_empty.json";
    public static final String GET_RESOLUTION_CENTER_EMPTY_JSON = "get_resolution_center_empty.json";
    public static final String GET_DEPOSIT_ZERO_JSON = "get_deposit_zero.json";
    public static final String FORBIDDEN_JSON = "forbidden.json";
    public static final String MESSAGE_ERRORS_JSON = "message_errors.json";
    public static final String GET_PRODUCT_GRAPH_JSON = "get_product_graph.json";
    public static final String GET_TRANSACTION_GRAPH_JSON = "get_transaction_graph.json";
    public static final String GET_PRODUCT_GRAPH_ONE_DIGIT_JSON = "get_product_graph_one_digit.json";
    public static final String GET_PRODUCT_GRAPH_TWO_DIGIT_JSON = "get_product_graph_two_digit.json";
    public static final String GET_PRODUCT_GRAPH_THREE_DIGIT_JSON = "get_product_graph_three_digit.json";
    public static final String GET_PRODUCT_GRAPH_FOUR_DIGIT_JSON = "get_product_graph_four_digit.json";
    public static final String GET_PRODUCT_GRAPH_FIVE_DIGIT_JSON = "get_product_graph_five_digit.json";
    public static final String GET_PRODUCT_GRAPH_SIX_DIGIT_JSON = "get_product_graph_six_digit.json";
    public static final String GET_PRODUCT_GRAPH_FOURTEEN_DIGIT_JSON = "get_product_graph_fourteen_digit.json";
    public static final String GET_PRODUCT_GRAPH_ONE_MILLION_JSON = "get_product_graph_one_million.json";
    public static final String GET_PRODUCT_GRAPH_155_JSON = "get_product_graph_155.json";
    public static final String GET_PRODUCT_GRAPH_ONE_BILLION_JSON = "get_product_graph_one_billion.json";
    public static final String GET_PRODUCT_GRAPH_TEN_BILLION_JSON = "get_product_graph_ten_billion.json";
    public static final String GET_PRODUCT_GRAPH_HUNDRED_BILLION_JSON = "get_product_graph_hundred_billion.json";
    public static final String GET_PRODUCT_GRAPH_ZERO_PERCENTAGE_JSON = "get_product_graph_zero_percentage.json";
    public static final String GET_TRANSACTION_GRAPH_ZERO_PERCENTAGE_JSON = "get_transaction_graph_zero_percentage.json";
    public static final String GET_TRANSACTION_GRAPH_MORE_THAN_ZERO_PERCENTAGE_JSON = "get_transaction_graph_more_than_zero_percentage.json";
    public static final String GET_PRODUCT_GRAPH_MORE_THAN_ZERO_PERCENTAGE_JSON = "get_product_graph_more_than_zero_percentage.json";
    public static final String GET_PRODUCT_GRAPH_MORE_BELOW_ZERO_PERCENTAGE_JSON = "get_product_graph_more_below_zero_percentage.json";
    public static final String GET_TRANSACTION_GRAPH_BELOW_THAN_ZERO_PERCENTAGE_JSON = "get_transaction_graph_below_than_zero_percentage.json";
    public static final String GET_PRODUCT_GRAPH_NO_DATA_AVAILABLE_PERCENTAGE_JSON = "get_product_graph_no_data_available_percentage.json";
    public static final String GET_TRANSACTION_GRAPH_NO_DATA_AVAILABLE_PERCENTAGE_JSON = "get_transaction_graph_no_data_available_percentage.json";
    public static final String GET_TRANSACTION_GRAPH_ONE_DIGIT_JSON = "get_transaction_graph_one_digit.json";
    public static final String GET_TRANSACTION_GRAPH_155_JSON = "get_transaction_graph_155.json";
    public static final String GET_TRANSACTION_GRAPH_FIVE_DIGIT_JSON = "get_transaction_graph_five_digit.json";
    public static final String GET_TRANSACTION_GRAPH_FOUR_DIGIT_JSON = "get_transaction_graph_four_digit.json";
    public static final String GET_TRANSACTION_GRAPH_FOURTEEN_DIGIT_JSON = "get_transaction_graph_fourteen_digit.json";
    public static final String GET_TRANSACTION_GRAPH_HUNDRED_BILLION_JSON = "get_transaction_graph_hundred_billion.json";
    public static final String GET_TRANSACTION_GRAPH_ONE_BILLION_JSON = "get_transaction_graph_one_billion.json";
    public static final String GET_TRANSACTION_GRAPH_ONE_MILLION_JSON = "get_transaction_graph_one_million.json";
    public static final String GET_TRANSACTION_GRAPH_SIX_DIGIT_JSON = "get_transaction_graph_six_digit.json";
    public static final String GET_TRANSACTION_GRAPH_THREE_DIGIT_JSON = "get_transaction_graph_three_digit.json";
    public static final String GET_TRANSACTION_GRAPH_TWO_DIGIT_JSON = "get_transaction_graph_two_digit.json";
    public static final String GET_TRANSACTION_GRAPH_TEN_BILLION_JSON = "get_transaction_graph_ten_billion.json";
    public static final String POPULAR_PRODUCT_155_JSON = "popular_product_155.json";
    public static final String POPULAR_PRODUCT_FIVE_DIGIT_JSON = "popular_product_five_digit.json";
    public static final String POPULAR_PRODUCT_FOUR_DIGIT_JSON = "popular_product_four_digit.json";
    public static final String POPULAR_PRODUCT_FOURTEEN_DIGIT_JSON = "popular_product_fourteen_digit.json";
    public static final String POPULAR_PRODUCT_HUNDRED_BILLION_JSON = "popular_product_hundred_billion.json";
    public static final String POPULAR_PRODUCT_ONE_BILLION_JSON = "popular_product_one_billion.json";
    public static final String POPULAR_PRODUCT_ONE_DIGIT_JSON = "popular_product_one_digit.json";
    public static final String POPULAR_PRODUCT_ONE_MILLION_JSON = "popular_product_one_million.json";
    public static final String POPULAR_PRODUCT_SIX_DIGIT_JSON = "popular_product_six_digit.json";
    public static final String POPULAR_PRODUCT_TEN_BILLION_JSON = "popular_product_ten_billion.json";
    public static final String POPULAR_PRODUCT_THREE_DIGIT_JSON = "popular_product_three_digit.json";
    public static final String POPULAR_PRODUCT_TWO_DIGIT_JSON = "popular_product_two_digit.json";
    public static final String BUYER_GRAPH_155_JSON = "buyer_graph_155.json";
    public static final String BUYER_GRAPH_FIVE_DIGIT_JSON = "buyer_graph_five_digit.json";
    public static final String BUYER_GRAPH_FOUR_DIGIT_JSON = "buyer_graph_four_digit.json";
    public static final String BUYER_GRAPH_FOURTEEN_DIGIT_JSON = "buyer_graph_fourteen_digit.json";
    public static final String BUYER_GRAPH_HUNDRED_BILLION_JSON = "buyer_graph_hundred_billion.json";
    public static final String BUYER_GRAPH_ONE_BILLION_JSON = "buyer_graph_one_billion.json";
    public static final String BUYER_GRAPH_ONE_DIGIT_JSON = "buyer_graph_one_digit.json";
    public static final String BUYER_GRAPH_ONE_MILLION_JSON = "buyer_graph_one_million.json";
    public static final String BUYER_GRAPH_SIX_DIGIT_JSON = "buyer_graph_six_digit.json";
    public static final String BUYER_GRAPH_TEN_BILLION_JSON = "buyer_graph_ten_billion.json";
    public static final String BUYER_GRAPH_THREE_DIGIT_JSON = "buyer_graph_three_digit.json";
    public static final String BUYER_GRAPH_TWO_DIGIT_JSON = "buyer_graph_two_digit.json";
    public static final String SHOP_CATEGORY_EMPTY_JSON = "shop_category_empty.json";
    public static final String SEARCH_KEYWORD_EMPTY_JSON = "search_keyword_empty.json";
    public static final String BUYER_GRAPH_EMPTY_STATE_JSON = "buyer_graph_empty_state.json";
    public static final String GET_TRANSACTION_GRAPH_EMPTY_STATE_JSON = "get_transaction_graph_empty_state.json";
    public static final String GET_PRODUCT_GRAPH_EMPTY_STATE_JSON = "get_product_graph_empty_state.json";
    public static final String POPULAR_PRODUCT_TWO_LINE_PRODUCT_NAME_JSON = "popular_product_two_line_product_name.json";
    public static final String ANOMALY_EMPTY_STATE_BUYER_DATA_JSON = "anomaly_empty_state_buyer_data.json";

    @Rule
    public ActivityTestRule<SellerHomeActivity> activityTestRule =
            new ActivityTestRule<SellerHomeActivity>(
                    SellerHomeActivity.class, true, false
            );

    @Inject
    GCMHandler gcmHandler;

    @Inject
    Gson gson;

    private MockWebServer server;

    private String loginSuccessKainan ;

    JsonFactory jsonFactory = new JsonFactory();
    Context context;

    @Before
    public void setup() throws Exception{
        server = new MockWebServer();
        server.start();

        Constants.BASE_URL = server.url("/").toString();
        RxJavaTestPlugins.setAsyncTaskScheduler();

        SellerMainApplication application = (SellerMainApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        application.setComponent(DaggerTestComponent.builder()
                .appModule(new AppModule(application))
                .networkModules(new NetworkModules())
                .testUtilModules(new TestUtilModules())
                .build());

        TestComponent component = (TestComponent)application.getComponent();
        component.inject(this);

        setupGCMHandler(gcmHandler);

        loginSuccessKainan =
                getStringFromFile(InstrumentationRegistry.getContext(), "login_kainan.json");

        context = InstrumentationRegistry.getContext();
    }

//    /**
//     * @throws Exception
//     */
//    @Test
//    public void testFirstRunSellerHome() throws Exception{
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_SHOP_INFO_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_NOTIFICATION_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_RESOLUTION_CENTER_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_DEPOSIT_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_ORDER_NEW_JSON)));
//
//        startSellerHome();
//
//        Spoon.screenshot(activityTestRule.getActivity(), "testFirstRunSellerHome");
//    }

    /**
     * run directly to seller home.
     */
//    protected void startSellerHome() {
//        LoginResponseParser LoginResponseParser = gson.fromJson(loginSuccessKainan, com.tokopedia.sellerapp.network.apiServices.parsers.user.LoginResponseParser.class);
//        Intent intent = new Intent();
//        intent.putExtra(LoginFragment.LOGIN_PROFILE, Parcels.wrap(LoginResponseParser));
//        activityTestRule.launchActivity(intent);
//    }

//    @Test
//    public void checkIfZeroNewOrder() throws Exception{
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_SHOP_INFO_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_NOTIFICATION_ZERO_SALES_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_RESOLUTION_CENTER_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_DEPOSIT_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_ORDER_NEW_JSON)));
//
//        startSellerHome();
//
//        ViewInteraction textView = onView(
//                allOf(withId(R.id.seller_home_new_order_number), withText("0"),
//                        childAtPosition(
//                                allOf(withId(R.id.seller_home_new_order_static),
//                                        childAtPosition(
//                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
//                                                0)),
//                                0),
//                        isDisplayed()));
//        textView.check(matches(withText("0")));
//
//        Spoon.screenshot(activityTestRule.getActivity(), "checkIfZeroNewOrder");
//    }

//    @Test
//    public void checkIfTwoNewOrder() throws Exception{
//
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_SHOP_INFO_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_NOTIFICATION_TWO_SALES_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_RESOLUTION_CENTER_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_DEPOSIT_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_ORDER_NEW_TWO_ITEM_JSON)));
//
//        startSellerHome();
//
//        ViewInteraction textView = onView(
//                allOf(withId(R.id.seller_home_new_order_number), withText("2"),
//                        childAtPosition(
//                                allOf(withId(R.id.seller_home_new_order_static),
//                                        childAtPosition(
//                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
//                                                0)),
//                                0),
//                        isDisplayed()));
//        textView.check(matches(withText("2")));
//
//        Spoon.screenshot(activityTestRule.getActivity(), "checkIfTwoNewOrder");
//    }
//
//    @Test
//    public void testScrollToBottom() throws Exception{
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_SHOP_INFO_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_NOTIFICATION_TWO_SALES_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_RESOLUTION_CENTER_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_DEPOSIT_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_ORDER_NEW_TWO_ITEM_JSON)));
//
//        startSellerHome();
//
//        onView(withId(R.id.seller_home_blank_space)).perform(NestedScrollViewScrollToAction.scrollTo());
////        NestedScrollViewScrollToAction.scrollTo();
//
//
//        Spoon.screenshot(activityTestRule.getActivity(), "testScrollToBottom");
//    }
//
//
//
//    @Test
//    public void testFirstCreatedShop() throws Exception{
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_SHOP_INFO_FIRST_CREATED_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_NOTIFICATION_EMPTY_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_RESOLUTION_CENTER_EMPTY_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_DEPOSIT_ZERO_JSON)));
//
//        startSellerHome();
//
//        Spoon.screenshot(activityTestRule.getActivity(), "testFirstCreatedShop");
//
//        onView(withId(R.id.seller_home_shop_name)).check(matches(withText("ngetestoki")));
//
//        onView(withId(R.id.seller_home_deposit)).check(matches(withText("Rp 0")));
//
//        onView(withId(R.id.reputation_points)).check(matches(withText("Reputasi : 0 Poin")));
//
//    }
//
//    @Test
//    public void testIfOneOfApiDown() throws Exception{
//
//        Constants.CONNECTION_TIMEOUT = Constants.CONNECTION_TIMEOUT_TEST;
//
//        server.enqueue(create200DelayedResponse(jsonFactory.getJson(context, GET_SHOP_INFO_FIRST_CREATED_JSON)));
//
//        startSellerHome();
//
//        Spoon.screenshot(activityTestRule.getActivity(), "testIfOneOfApiDown");
//
//        Constants.CONNECTION_TIMEOUT = Constants.CONNECTION_TIMEOUT_REAL;
//    }
//
//    /**
//     * 403 or 404 error code makes no error messages at seller home.
//     * @throws Exception
//     */
//    @Test
//    public void testIfApiDownForbidden() throws Exception{
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_SHOP_INFO_FIRST_CREATED_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_NOTIFICATION_EMPTY_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_RESOLUTION_CENTER_EMPTY_JSON)));
//        server.enqueue(create403Forbidden(jsonFactory.getJson(context, FORBIDDEN_JSON)));
//
//        startSellerHome();
//
//        Spoon.screenshot(activityTestRule.getActivity(), "testIfApiDownForbidden");
//    }
//
//    @Test
//    public void testFAB() throws Exception{
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_SHOP_INFO_FIRST_CREATED_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_NOTIFICATION_EMPTY_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_RESOLUTION_CENTER_EMPTY_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_DEPOSIT_ZERO_JSON)));
//
//        startSellerHome();
//
//        onView(withId(R.id.seller_home_boom)).perform(click());
//
//        Spoon.screenshot(activityTestRule.getActivity(), "testFAB");
//    }
//
//    @Test
//    public void testMessageErrorInOneOfApi() throws Exception{
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_SHOP_INFO_FIRST_CREATED_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_NOTIFICATION_EMPTY_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_RESOLUTION_CENTER_EMPTY_JSON)));
//        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, MESSAGE_ERRORS_JSON)));
//
//        startSellerHome();
//
//
//
//        Spoon.screenshot(activityTestRule.getActivity(), "testMessageErrorInOneOfApi");
//    }


    /**
     * encapsulate the string name from asset in here
     */
    public static final class JsonFactory {

        public String getJson(Context context, String name) throws Exception{
            switch (name){
                case GET_SHOP_INFO_FIRST_CREATED_JSON:
                case GET_NOTIFICATION_EMPTY_JSON:
                case GET_RESOLUTION_CENTER_EMPTY_JSON:
                case GET_DEPOSIT_ZERO_JSON:
                case GET_SHOP_INFO_JSON:
                case GET_NOTIFICATION_JSON:
                case GET_NOTIFICATION_ZERO_SALES_JSON:
                case GET_RESOLUTION_CENTER_JSON:
                case GET_DEPOSIT_JSON:
                case GET_ORDER_NEW_JSON:
                case GET_ORDER_NEW_TWO_ITEM_JSON:
                case GET_NOTIFICATION_TWO_SALES_JSON:
                case FORBIDDEN_JSON:
                case MESSAGE_ERRORS_JSON:
                case GET_PRODUCT_GRAPH_JSON:
                case GET_TRANSACTION_GRAPH_JSON:
                case BUYER_GRAPH_JSON:
                case POPULAR_PRODUCT_JSON:
                case SHOP_CATEGORY_JSON:
                case SEARCH_KEYWORD_JSON:
                case CAT_ID_JSON:
                case GET_PRODUCT_GRAPH_ONE_DIGIT_JSON:
                case GET_PRODUCT_GRAPH_TWO_DIGIT_JSON:
                case GET_PRODUCT_GRAPH_THREE_DIGIT_JSON:
                case GET_PRODUCT_GRAPH_FOUR_DIGIT_JSON:
                case GET_PRODUCT_GRAPH_FIVE_DIGIT_JSON:
                case GET_PRODUCT_GRAPH_SIX_DIGIT_JSON:
                case GET_PRODUCT_GRAPH_FOURTEEN_DIGIT_JSON:
                case GET_PRODUCT_GRAPH_ONE_MILLION_JSON:
                case GET_PRODUCT_GRAPH_155_JSON:
                case GET_PRODUCT_GRAPH_ONE_BILLION_JSON:
                case GET_PRODUCT_GRAPH_TEN_BILLION_JSON:
                case GET_PRODUCT_GRAPH_HUNDRED_BILLION_JSON:
                case GET_TRANSACTION_GRAPH_ZERO_PERCENTAGE_JSON:
                case GET_PRODUCT_GRAPH_ZERO_PERCENTAGE_JSON:
                case GET_TRANSACTION_GRAPH_MORE_THAN_ZERO_PERCENTAGE_JSON:
                case GET_PRODUCT_GRAPH_MORE_THAN_ZERO_PERCENTAGE_JSON:
                case GET_PRODUCT_GRAPH_MORE_BELOW_ZERO_PERCENTAGE_JSON:
                case GET_TRANSACTION_GRAPH_BELOW_THAN_ZERO_PERCENTAGE_JSON:
                case GET_PRODUCT_GRAPH_NO_DATA_AVAILABLE_PERCENTAGE_JSON:
                case GET_TRANSACTION_GRAPH_NO_DATA_AVAILABLE_PERCENTAGE_JSON:
                case GET_TRANSACTION_GRAPH_ONE_DIGIT_JSON:
                case GET_TRANSACTION_GRAPH_155_JSON:
                case GET_TRANSACTION_GRAPH_FIVE_DIGIT_JSON:
                case GET_TRANSACTION_GRAPH_FOUR_DIGIT_JSON:
                case GET_TRANSACTION_GRAPH_FOURTEEN_DIGIT_JSON:
                case GET_TRANSACTION_GRAPH_HUNDRED_BILLION_JSON:
                case GET_TRANSACTION_GRAPH_ONE_BILLION_JSON:
                case GET_TRANSACTION_GRAPH_ONE_MILLION_JSON:
                case GET_TRANSACTION_GRAPH_SIX_DIGIT_JSON:
                case GET_TRANSACTION_GRAPH_THREE_DIGIT_JSON:
                case GET_TRANSACTION_GRAPH_TWO_DIGIT_JSON:
                case GET_TRANSACTION_GRAPH_TEN_BILLION_JSON:
                case POPULAR_PRODUCT_155_JSON:
                case POPULAR_PRODUCT_FIVE_DIGIT_JSON:
                case POPULAR_PRODUCT_FOUR_DIGIT_JSON:
                case POPULAR_PRODUCT_FOURTEEN_DIGIT_JSON:
                case POPULAR_PRODUCT_HUNDRED_BILLION_JSON:
                case POPULAR_PRODUCT_ONE_BILLION_JSON:
                case POPULAR_PRODUCT_ONE_DIGIT_JSON:
                case POPULAR_PRODUCT_ONE_MILLION_JSON:
                case POPULAR_PRODUCT_SIX_DIGIT_JSON:
                case POPULAR_PRODUCT_TEN_BILLION_JSON:
                case POPULAR_PRODUCT_THREE_DIGIT_JSON:
                case POPULAR_PRODUCT_TWO_DIGIT_JSON:
                case BUYER_GRAPH_155_JSON:
                case BUYER_GRAPH_FIVE_DIGIT_JSON:
                case BUYER_GRAPH_FOUR_DIGIT_JSON:
                case BUYER_GRAPH_FOURTEEN_DIGIT_JSON:
                case BUYER_GRAPH_HUNDRED_BILLION_JSON:
                case BUYER_GRAPH_ONE_BILLION_JSON:
                case BUYER_GRAPH_ONE_DIGIT_JSON:
                case BUYER_GRAPH_ONE_MILLION_JSON:
                case BUYER_GRAPH_SIX_DIGIT_JSON:
                case BUYER_GRAPH_TEN_BILLION_JSON:
                case BUYER_GRAPH_THREE_DIGIT_JSON:
                case BUYER_GRAPH_TWO_DIGIT_JSON:
                case SHOP_CATEGORY_EMPTY_JSON:
                case SEARCH_KEYWORD_EMPTY_JSON:
                case BUYER_GRAPH_EMPTY_STATE_JSON:
                case GET_TRANSACTION_GRAPH_EMPTY_STATE_JSON:
                case GET_PRODUCT_GRAPH_EMPTY_STATE_JSON:
                case POPULAR_PRODUCT_TWO_LINE_PRODUCT_NAME_JSON:
                case ANOMALY_EMPTY_STATE_BUYER_DATA_JSON:
                    return getStringFromFile(context, name);
                default:
                    throw new RuntimeException("please add manually type here !!");
            }
        }
    }


    @After
    public void tearDown() throws Exception{
        RxJavaTestPlugins.resetJavaTestPlugins();
        server.shutdown();
    }


    public static MockResponse createSuccess200Response(String response){
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(response);
        return mockResponse;
    }

    public static MockResponse create200DelayedResponse(String response){
        MockResponse mockResponse = new MockResponse()
                .setBodyDelay(3, SECONDS)
                .setResponseCode(200)
                .setBody(response);
        return mockResponse;
    }

    public static MockResponse create404Forbidden(String response){
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(404)
                .setBody(response);
        return mockResponse;
    }

    public static MockResponse create403Forbidden(String response){
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(403)
                .setBody(response);
        return mockResponse;
    }
}
