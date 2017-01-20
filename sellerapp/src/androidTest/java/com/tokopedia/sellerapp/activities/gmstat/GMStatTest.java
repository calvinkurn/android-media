package com.tokopedia.sellerapp.activities.gmstat;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.squareup.spoon.Spoon;
import com.tokopedia.seller.gmstat.hades.HadesNetwork;
import com.tokopedia.seller.gmstat.utils.GMStatConstant;
import com.tokopedia.sellerapp.DaggerTestComponent;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.RxJavaTestPlugins;
import com.tokopedia.sellerapp.SellerMainApplication;
import com.tokopedia.sellerapp.TestComponent;
import com.tokopedia.sellerapp.TestUtilModules;
import com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest;
import com.tokopedia.sellerapp.daggerModules.AppModule;
import com.tokopedia.sellerapp.daggerModules.NetworkModules;
import com.tokopedia.sellerapp.gmstat.activities.GMStatActivity2;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tokopedia.sellerapp.Utils.ExtraAssertions.isGone;
import static com.tokopedia.sellerapp.Utils.ExtraAssertions.isInvisible;
import static com.tokopedia.sellerapp.Utils.RecyclerViewTestUtils.withRecyclerView;
import static com.tokopedia.sellerapp.Utils.takeScreenshot;
import static com.tokopedia.sellerapp.Utils.withTextColor;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.ANOMALY_EMPTY_STATE_BUYER_DATA_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_155_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_EMPTY_STATE_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_FIVE_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_FOURTEEN_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_FOUR_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_HUNDRED_BILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_ONE_BILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_ONE_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_ONE_MILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_SIX_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_TEN_BILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_THREE_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.BUYER_GRAPH_TWO_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_155_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_EMPTY_STATE_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_FIVE_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_FOURTEEN_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_FOUR_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_HUNDRED_BILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_MORE_BELOW_ZERO_PERCENTAGE_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_MORE_THAN_ZERO_PERCENTAGE_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_NO_DATA_AVAILABLE_PERCENTAGE_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_ONE_BILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_ONE_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_ONE_MILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_SIX_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_TEN_BILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_THREE_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_TWO_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_PRODUCT_GRAPH_ZERO_PERCENTAGE_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_155_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_BELOW_THAN_ZERO_PERCENTAGE_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_EMPTY_STATE_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_FIVE_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_FOURTEEN_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_FOUR_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_HUNDRED_BILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_MORE_THAN_ZERO_PERCENTAGE_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_NO_DATA_AVAILABLE_PERCENTAGE_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_ONE_BILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_ONE_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_ONE_MILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_SIX_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_TEN_BILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_THREE_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_TWO_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.GET_TRANSACTION_GRAPH_ZERO_PERCENTAGE_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_155_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_FIVE_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_FOURTEEN_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_FOUR_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_HUNDRED_BILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_ONE_BILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_ONE_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_ONE_MILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_SIX_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_TEN_BILLION_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_THREE_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_TWO_DIGIT_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.POPULAR_PRODUCT_TWO_LINE_PRODUCT_NAME_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.SEARCH_KEYWORD_EMPTY_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.SHOP_CATEGORY_EMPTY_JSON;
import static com.tokopedia.sellerapp.activities.sellerhome.SellerHomeTest.createSuccess200Response;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by normansyahputa on 11/8/16.
 * enable hades network 25/12/2016
 */

@RunWith(AndroidJUnit4.class)
public class GMStatTest {
    public static final String BUYER_GRAPH_JSON = "buyer_graph.json";
    public static final String POPULAR_PRODUCT_JSON = "popular_product.json";
    public static final String SHOP_CATEGORY_JSON = "shop_category.json";
    public static final String SEARCH_KEYWORD_JSON = "search_keyword.json";
    public static final String CAT_ID_JSON = "cat_id.json";
    @Rule
    public ActivityTestRule<GMStatActivity2> activityTestRule =
            new ActivityTestRule<GMStatActivity2>(
                    GMStatActivity2.class, true, false
            );

    MockWebServer server;
    private Context context;

    SellerHomeTest.JsonFactory jsonFactory = new SellerHomeTest.JsonFactory();


    @Before
    public void setup() throws Exception{
        server = new MockWebServer();
        server.start();

        GMStatConstant.baseUrl = server.url("/").toString();
        HadesNetwork.baseUrl = server.url("/").toString();
        RxJavaTestPlugins.setAsyncTaskScheduler();

        SellerMainApplication application = (SellerMainApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        application.setComponent(DaggerTestComponent.builder()
                .appModule(new AppModule(application))
                .networkModules(new NetworkModules())
                .testUtilModules(new TestUtilModules())
                .build());

        TestComponent component = (TestComponent)application.getComponent();
        component.inject(this);

        context = InstrumentationRegistry.getContext();
    }

    @Test
    public void checkNumberFormatForSuccessfulTransactionForOneDigit() throws Exception{
        singleDigit();
        startGoldMerchant();
        successfulTransactionDigit("4_segment_views_single_digit", 1, "_single_digit");
    }

    public void successfulTransactionDigit(String tag, int value, String suffix) {
        successfulTransactionDigit(tag, Integer.toString(value), suffix);
    }

    public void successfulTransactionDigit(String tag, String value, String suffix) {
        Spoon.screenshot(activityTestRule.getActivity(), tag);
        // conversion rate is excluded beacause it is double
        for(int index=0;index<3;index++){
            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.text))
                    .check(matches(withText(value)));
        }
        //[START] This is for single checking
//        ViewInteraction textView = onView(
//                allOf(withId(R.id.text), withText(value),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.gmstat_recyclerview),
//                                        0),
//                                1),
//                        isDisplayed()));
//        textView.check(matches(withText(value)));
        //[END] This is for single checking

        int index = 4;
        onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.text))
                .check(matches(withText("Rp "+value)));

        onView(withId(R.id.popular_product_container))
                .perform(scrollTo(), click());
        onView(allOf(withId(R.id.number_of_selling))).check(matches(withText(value)));
        Spoon.screenshot(activityTestRule.getActivity(), "popular_product_"+suffix);

        onView(withId(R.id.transaction_data_container))
                .perform(scrollTo(), click());
        onView(allOf(withId(R.id.transaction_count))).check(matches(withText(value)));
        Spoon.screenshot(activityTestRule.getActivity(), "transaction_data_"+suffix);

        onView(withId(R.id.buyer_data_container))
                .perform(scrollTo(), click());
        onView(allOf(withId(R.id.buyer_count))).check(matches(withText(value)));
        Spoon.screenshot(activityTestRule.getActivity(), "buyer_count_"+suffix);
    }

    @Test
    public void checkNumberFormatForSuccessfulTransactionForTwoDigit() throws Exception{
        twoDigit();
        startGoldMerchant();
        successfulTransactionDigit("4_segment_views_two_digit", 19, "_two_digit");


    }

    @Test
    public void checkNumberFormatForSuccessfulTransactionForThreeDigit() throws Exception{
        threeDigit();
        startGoldMerchant();
        successfulTransactionDigit("4_segment_views_three_digit", 199, "_three_digit");
    }

    @Test
    public void checkNumberFormatForSuccessfulTransactionForFourDigit() throws Exception{
        fourDigit();
        startGoldMerchant();
        successfulTransactionDigit("4_segment_views_four_digit", "1.999", "_four_digit");
    }

    @Test
    public void checkNumberFormatForSuccessfulTransactionForFiveDigit() throws Exception{
        fiveDigit();
        startGoldMerchant();
        successfulTransactionDigit("4_segment_views_five_digit", "19.999", "_five_digit");
    }

    @Test
    public void checkNumberFormatForSuccessfulTransactionForSixDigit() throws Exception{
        sixDigit();
        startGoldMerchant();
        successfulTransactionDigit("4_segment_views_six_digit", "999.999", "_six_digit");
    }

    @Test
    public void checkNumberFormatForSuccessfulTransactionForFourTeenDigit() throws Exception{
        fourTeenDigit();
        startGoldMerchant();
        successfulTransactionDigit("4_segment_views_fourteen_digit", "1.0E7jt", "_fourteen_digit");
    }

    @Test
    public void checkNumberFormatForSuccessfulTransactionFor1Million() throws Exception{
        oneMillion();
        startGoldMerchant();
        successfulTransactionDigit("4_segment_views_one_million", "1.0jt","_one_million");
    }

    /**
     * 1.55 jt but just get 1.5 ( one digit after comma )
     * @throws Exception
     */
    @Test
    public void checkNumberFormatForSuccessfulTransactionFor1Point55Million() throws Exception{
        oneMPointFiftyFiveillion();
        startGoldMerchant();
        successfulTransactionDigit("4_segment_views_one_point_fifty_five_million", "1.5jt", "_fifty_five_million");
    }

    @Test
    public void checkNumberFormatForSuccessfulTransactionFor2Billion() throws Exception{
        twoBillion();
        startGoldMerchant();
        successfulTransactionDigit("4_segment_views_one_billion", "2345.6jt", "_one_billion");
    }

    @Test
    public void checkNumberFormatForSuccessfulTransactionFor20Billion() throws Exception{
        twentyBillion();
        startGoldMerchant();
        successfulTransactionDigit("4_segment_views_twenty_billion", "23456.0jt", "_twenty_billion");
    }

    @Test
    public void checkNumberFormatForSuccessfulTransactionFor200Billion() throws Exception{
        tweoHundredBillion();
        startGoldMerchant();
        successfulTransactionDigit("4_segment_views_hundreds_billion", "234560.0jt", "_hundreds_billion");
    }

    @Test
    public void checkTwoLinePopularProduct() throws Exception{
        allSuccessfulResponsesPopularProductNameMoreThanTwoLines();
        startGoldMerchant();

        onView(withId(R.id.popular_product_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "popular_product");
    }

    @Test
    public void checkNonGMEmptyShopCategory() throws Exception{
        allSuccessfulExceptEmptyShopCategory();
        startNonGoldMerchant();

        onView(withId(R.id.transaction_data_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "transaction_data");
        takeScreenshot("transaction_data", activityTestRule.getActivity());

        onView(withId(R.id.market_insight_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "market_insight");
        takeScreenshot("market_insight", activityTestRule.getActivity());
    }

    @Test
    public void checkEmptyShopCategory() throws Exception{
        allSuccessfulExceptEmptyShopCategory();
        startGoldMerchant();

        onView(withId(R.id.popular_product_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "popular_product");
        takeScreenshot("popular_product", activityTestRule.getActivity());

        onView(withId(R.id.transaction_data_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "transaction_data");
        takeScreenshot("transaction_data", activityTestRule.getActivity());

        onView(withId(R.id.buyer_data_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "buyer_data");
        takeScreenshot("buyer_data", activityTestRule.getActivity());
        ViewInteraction textView = onView(
                allOf(withId(R.id.female_pie), withText("0 %"),
                        withParent(withId(R.id.data_buyer_container_inner)),
                        isDisplayed()));
        textView.check(matches(withTextColor(activityTestRule.getActivity().getResources().getColor(R.color.grey_400))));


        onView(withId(R.id.market_insight_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "market_insight");
        takeScreenshot("market_insight", activityTestRule.getActivity());
    }

    @Test
    public void checkEmptyGetKeywords() throws Exception{
        allSuccessfulExceptEmptyGetKeywords();
        startGoldMerchant();

        onView(withId(R.id.market_insight_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "market_insight");
    }

    @Test
    public void testAnomalyStateBuyer() throws Exception{
        allSuccessfulResponsesBuyerEmptyState();

        startGoldMerchant();

        onView(withId(R.id.buyer_data_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "anomaly_state_buyer");
    }

    @Test
    public void scrollToEachSegmenGoldMerchant() throws Exception{
        allSuccessfulResponses();

        startGoldMerchant();

        onView(withId(R.id.gmstat_recyclerview))
                .perform(scrollTo(), click());
        Spoon.screenshot(activityTestRule.getActivity(), "4_segment_views");

        onView(withId(R.id.gross_income_graph2))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "gross_income");

        onView(withId(R.id.popular_product_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "popular_product");

        onView(withId(R.id.transaction_data_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "transaction_data");

        onView(withId(R.id.buyer_data_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "buyer_data");

        onView(withId(R.id.market_insight_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "market_insight");
    }

    public void startGoldMerchant() {
        Intent intent = new Intent();
        intent.putExtra(GMStatActivity2.IS_GOLD_MERCHANT, true);
        intent.putExtra(GMStatActivity2.SHOP_ID, "512612");
        activityTestRule.launchActivity(intent);
    }

    @Test
    public void scrollToEachSegmentNonGoldMerchant() throws Exception{
        allSuccessfulResponses();

        startNonGoldMerchant();

        onView(withId(R.id.gmstat_recyclerview))
                .perform(scrollTo(), click());
        Spoon.screenshot(activityTestRule.getActivity(), "4_segment_views_non_gold_merchant");

        onView(withId(R.id.gross_income_graph2))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "gross_income_non_gold_merchant");

        onView(withId(R.id.popular_product_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "popular_product_non_gold_merchant");

        onView(withId(R.id.transaction_data_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "transaction_data_non_gold_merchant");

        onView(withId(R.id.buyer_data_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "buyer_data_non_gold_merchant");

        onView(withId(R.id.market_insight_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "market_insight_non_gold_merchant");
    }

    @Test
    public void testSuccessfulTransactionZeroPercentage() throws Exception{
        allSuccessfulResponsesZeroPercentage();
        startGoldMerchant();

        onView(withId(R.id.gmstat_recyclerview))
                .perform(scrollTo(), click());
        Spoon.screenshot(activityTestRule.getActivity(), "4_segment_views_non_gold_merchant");

        for(int index=0;index<4;index++){
            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.percentage))
                    .check(matches(withTextColor(activityTestRule.getActivity().getResources().getColor(R.color.arrow_up))));

            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.percentage))
                    .check(matches(withText("0,00%")));

            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.arrow_icon))
                    .check(isGone());
        }

        onView(withId(R.id.transaction_data_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "transaction_data_non_gold_merchant");

        ViewInteraction textView = onView(
                allOf(withId(R.id.percentage), withText("0,00%"),
                        withParent(withId(R.id.transaction_data_container_gold_merchant2)),
                        isDisplayed()));
        textView.check(matches(withTextColor(activityTestRule.getActivity().getResources().getColor(R.color.arrow_up))));
        onView(allOf(withId(R.id.transaction_count_icon),
                        withParent(withId(R.id.transaction_data_container_gold_merchant2)))).check(isInvisible());
    }

    @Test
    public void testSuccessfulTransactionNoDataAvailablePercentage() throws Exception{
        allSuccessfulResponsesNoDataAvailablePercentage();
        startGoldMerchant();

        onView(withId(R.id.gmstat_recyclerview))
                .perform(scrollTo(), click());
        Spoon.screenshot(activityTestRule.getActivity(), "4_segment_views_non_gold_merchant");

        for(int index=0;index<4;index++){
            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.percentage))
                    .check(matches(withTextColor(activityTestRule.getActivity().getResources().getColor(R.color.grey_400))));

            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.percentage))
                    .check(matches(withText("Tidak ada data")));

            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.arrow_icon))
                    .check(isGone());
        }

        onView(withId(R.id.transaction_data_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "transaction_data_non_gold_merchant");

        ViewInteraction textView = onView(
                allOf(withId(R.id.percentage), withText("Tidak ada data"),
                        withParent(withId(R.id.transaction_data_container_gold_merchant2)),
                        isDisplayed()));
        textView.check(matches(withTextColor(activityTestRule.getActivity().getResources().getColor(R.color.grey_400))));
        onView(allOf(withId(R.id.transaction_count_icon),
                withParent(withId(R.id.transaction_data_container_gold_merchant2)))).check(isInvisible());
    }

    @Test
    public void testSuccessfullTransactionwithMoreThanZeroPercentage() throws Exception {
        allSuccessfulResponsesMoreThanZeroPercentage();
        startGoldMerchant();

        onView(withId(R.id.gmstat_recyclerview))
                .perform(scrollTo(), click());
        Spoon.screenshot(activityTestRule.getActivity(), "4_segment_views_non_gold_merchant");

        for(int index=0;index<4;index++){
            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.percentage))
                    .check(matches(withTextColor(activityTestRule.getActivity().getResources().getColor(R.color.arrow_up))));

            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.percentage))
                    .check(matches(withText("13,00%")));

            // TODO copmare two images now not available
//            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.arrow_icon))
//                    .check(isVisible()).check(matches(withDrawable(R.drawable.ic_rectangle_up)));
        }

        onView(withId(R.id.transaction_data_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "transaction_data_non_gold_merchant");

        ViewInteraction textView = onView(
                allOf(withId(R.id.percentage), withText("13,00%"),
                        withParent(withId(R.id.transaction_data_container_gold_merchant2)),
                        isDisplayed()));
        textView.check(matches(withTextColor(activityTestRule.getActivity().getResources().getColor(R.color.arrow_up))));
        // TODO copmare two images now not available
//        onView(allOf(withId(R.id.transaction_count_icon),
//                withParent(withId(R.id.transaction_data_container_gold_merchant2))))
//                .check(isVisible()).check(matches(withDrawable(R.drawable.ic_rectangle_up)));
    }

    @Test
    public void testSuccessfullTransactionwithBelowThanZeroPercentage() throws Exception {
        allSuccessfulResponsesBelowThanZeroPercentage();
        startGoldMerchant();

        onView(withId(R.id.gmstat_recyclerview))
                .perform(scrollTo(), click());
        Spoon.screenshot(activityTestRule.getActivity(), "4_segment_views_non_gold_merchant");

        for(int index=0;index<4;index++){
            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.percentage))
                    .check(matches(withTextColor(activityTestRule.getActivity().getResources().getColor(R.color.arrow_down))));

            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.percentage))
                    .check(matches(withText("13,00%")));

            // TODO copmare two images now not available
//            onView(withRecyclerView(R.id.gmstat_recyclerview).atPositionOnView(index, R.id.arrow_icon))
//                    .check(isVisible()).check(matches(withDrawable(R.drawable.ic_rectangle_up)));
        }

        onView(withId(R.id.transaction_data_container))
                .perform(scrollTo(), click());

        Spoon.screenshot(activityTestRule.getActivity(), "transaction_data_non_gold_merchant");

        ViewInteraction textView = onView(
                allOf(withId(R.id.percentage), withText("13,00%"),
                        withParent(withId(R.id.transaction_data_container_gold_merchant2)),
                        isDisplayed()));
        textView.check(matches(withTextColor(activityTestRule.getActivity().getResources().getColor(R.color.arrow_down))));
        // TODO copmare two images now not available
//        onView(allOf(withId(R.id.transaction_count_icon),
//                withParent(withId(R.id.transaction_data_container_gold_merchant2))))
//                .check(isVisible()).check(matches(withDrawable(R.drawable.ic_rectangle_up)));
    }

    public void startNonGoldMerchant() {
        Intent intent = new Intent();
        intent.putExtra(GMStatActivity2.IS_GOLD_MERCHANT, false);
        intent.putExtra(GMStatActivity2.SHOP_ID, "512612");
        activityTestRule.launchActivity(intent);
    }

    public void allSuccessfulResponsesBelowThanZeroPercentage() throws Exception {
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_MORE_BELOW_ZERO_PERCENTAGE_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_BELOW_THAN_ZERO_PERCENTAGE_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void allSuccessfulResponsesMoreThanZeroPercentage() throws Exception {
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_MORE_THAN_ZERO_PERCENTAGE_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_MORE_THAN_ZERO_PERCENTAGE_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void allSuccessfulResponsesNoDataAvailablePercentage() throws Exception {
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_NO_DATA_AVAILABLE_PERCENTAGE_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_NO_DATA_AVAILABLE_PERCENTAGE_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void allSuccessfulResponsesZeroPercentage() throws Exception {
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_ZERO_PERCENTAGE_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_ZERO_PERCENTAGE_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void allSuccessfulResponsesPopularProductNameMoreThanTwoLines() throws Exception {
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_TWO_LINE_PRODUCT_NAME_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void allSuccessfulResponsesBuyerEmptyState() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, ANOMALY_EMPTY_STATE_BUYER_DATA_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void allSuccessfulResponses() throws Exception {
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void allSuccessfulExceptEmptyGetKeywords() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_EMPTY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void allSuccessfulExceptEmptyShopCategory() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_EMPTY_STATE_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_EMPTY_STATE_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_EMPTY_STATE_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_EMPTY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void singleDigit() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_ONE_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_ONE_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_ONE_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_ONE_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void twoDigit() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_TWO_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_TWO_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_TWO_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_TWO_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void threeDigit() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_THREE_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_THREE_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_THREE_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_THREE_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void fourDigit() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_FOUR_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_FOUR_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_FOUR_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_FOUR_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void fiveDigit() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_FIVE_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_FIVE_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_FIVE_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_FIVE_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void sixDigit() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_SIX_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_SIX_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_SIX_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_SIX_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void fourTeenDigit() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_FOURTEEN_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_FOURTEEN_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_FOURTEEN_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_FOURTEEN_DIGIT_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void oneMillion() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_ONE_MILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_ONE_MILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_ONE_MILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_ONE_MILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void oneMPointFiftyFiveillion() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_155_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_155_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_155_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_155_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void twoBillion() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_ONE_BILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_ONE_BILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_ONE_BILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_ONE_BILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void twentyBillion() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_TEN_BILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_TEN_BILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_TEN_BILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_TEN_BILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }

    public void tweoHundredBillion() throws Exception{
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_PRODUCT_GRAPH_HUNDRED_BILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, GET_TRANSACTION_GRAPH_HUNDRED_BILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, POPULAR_PRODUCT_HUNDRED_BILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, BUYER_GRAPH_HUNDRED_BILLION_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SHOP_CATEGORY_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, SEARCH_KEYWORD_JSON)));
        server.enqueue(createSuccess200Response(jsonFactory.getJson(context, CAT_ID_JSON)));
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @After
    public void tearDown() throws Exception{
        RxJavaTestPlugins.resetJavaTestPlugins();
        server.shutdown();
    }
}
