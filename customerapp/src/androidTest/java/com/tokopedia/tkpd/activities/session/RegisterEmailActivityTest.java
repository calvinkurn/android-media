package com.tokopedia.tkpd.activities.session;

import android.Manifest;
import androidx.annotation.IdRes;
import androidx.test.espresso.ViewInteraction;
import androidx.test.runner.AndroidJUnit4;
import android.view.View;

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.session.R;
import com.tokopedia.session.google.GoogleSignInActivity;
import com.tokopedia.tkpd.model.ErrorMessageModel;
import com.tokopedia.tkpd.model.session.RegisterModel;
import com.tokopedia.tkpd.rule.GuessTokopediaTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.openLinkWithText;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.tokopedia.tkpd.Utils.allowPermissionsIfNeeded;
import static com.tokopedia.tkpd.Utils.matchToolbarTitle;
import static com.tokopedia.tkpd.Utils.mockGoogleActivityResult;
import static com.tokopedia.tkpd.Utils.nthChildOf;
import static com.tokopedia.tkpd.Utils.snackbarMatcher;
import static com.tokopedia.tkpd.rule.GuessTokopediaTestRule.SESSION_URL_ACCOUNTS_DOMAIN;
import static org.hamcrest.Matchers.not;


/**
 * Created by normansyahputa on 4/9/18.
 */
@RunWith(AndroidJUnit4.class)
public class RegisterEmailActivityTest {
    @Rule
    public GuessTokopediaTestRule<RegisterEmailActivity> mIntentsRule = new GuessTokopediaTestRule<RegisterEmailActivity>(
            RegisterEmailActivity.class, true, false, 3
    );

    /**
     * this test check all UI is visible at first time
     * also test local validation
     *
     * @throws Exception
     */
    @Test
    public void testInitialUI() throws Exception {
        defaultGoogleRegister();

        startEmptyActivity();

        ScreenShotter.takeScreenshot("1", mIntentsRule.getActivity());

        allowPermissionsIfNeeded(Manifest.permission.GET_ACCOUNTS);

        onView(tkpdHintTextInputLayout(R.id.wrapper_email)).check(matches(isDisplayed()));

        onView(tkpdHintTextInputLayout(R.id.wrapper_name)).check(matches(isDisplayed()));

        onView(tkpdHintTextInputLayout(R.id.wrapper_phone)).check(matches(isDisplayed()));

        onView(tkpdHintTextInputLayout(R.id.wrapper_password)).check(matches(isDisplayed()));

        onView(withId(R.id.register_next_detail_t_and_p)).perform(openLinkWithText("Syarat dan Ketentuan"));

        Thread.sleep(1_000);
        mIntentsRule.getDevice().pressBack();

        onView(withId(R.id.register_next_detail_t_and_p)).perform(openLinkWithText("Kebijakan Privasi"));

        Thread.sleep(1_000);
        mIntentsRule.getDevice().pressBack();

        onView(withId(R.id.register_button))
                .check(matches(isDisplayed()))
                .check(matches(not(isEnabled())))
                .perform(click());

        // clear text
        onView(tkpdHintTextInputLayout(R.id.wrapper_email)).perform(clearText());
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_email)).check(matches(withText(R.string.error_field_required)));

        // type wrong email format
        onView(tkpdHintTextInputLayout(R.id.wrapper_email))
                .perform(clearText())
                .perform(typeText("cincin.jati+47@gmailcom"));
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_email))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_invalid_email)));

        // type right email format
        onView(tkpdHintTextInputLayout(R.id.wrapper_email))
                .perform(clearText())
                .perform(typeText("cincin.jati+47@gmailc.com"));
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_email)).check(matches(not(isDisplayed())));

        ScreenShotter.takeScreenshot("2", mIntentsRule.getActivity());

        // clear text
        onView(tkpdHintTextInputLayout(R.id.wrapper_name))
                .perform(clearText());
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_name))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_field_required)));

        onView(tkpdHintTextInputLayout(R.id.wrapper_name))
                .perform(clearText())
                .perform(typeText("%123_"));
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_name))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_illegal_character)));

        onView(tkpdHintTextInputLayout(R.id.wrapper_name))
                .perform(clearText())
                .perform(typeText(
                        "abcde" + // 1
                                "abcde" + // 2
                                "abcde" + // 3
                                "abcde" + // 4
                                "abcde" + // 5
                                "abcde" + // 6
                                "abcde" + // 7
                                "abcde" + // 8
                                "abcde")); // 9
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_name))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_max_35_character)));


        // right name
        onView(tkpdHintTextInputLayout(R.id.wrapper_name))
                .perform(clearText())
                .perform(typeText("lalalaLand"));


        onView(tkpdHintTextInputLayout(R.id.wrapper_phone)).perform(clearText());
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_phone))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_field_required)));

        onView(tkpdHintTextInputLayout(R.id.wrapper_phone))
                .perform(clearText())
                .perform(replaceText("6281-2843-0264-095"));
        ScreenShotter.takeScreenshot("3", mIntentsRule.getActivity());
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_phone))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_invalid_phone_number)));

        onView(tkpdHintTextInputLayout(R.id.wrapper_phone))
                .perform(clearText())
                .perform(replaceText("08128"));
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_phone))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_invalid_phone_number)));

        onView(tkpdHintTextInputLayout(R.id.wrapper_phone))
                .perform(clearText())
                .perform(replaceText("6281-2843-0264-0"));
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_phone))
                .check(matches(not(isDisplayed())));

        // right phone number
        onView(tkpdHintTextInputLayout(R.id.wrapper_phone))
                .perform(clearText(), replaceText("0812-8440-2240"), closeSoftKeyboard());
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_phone))
                .check(matches(not(isDisplayed())));

        ViewInteraction registerPassword = onView(withId(R.id.register_password));
        registerPassword.perform(typeText("lalala"), clearText());
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_password))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_field_required)));

        registerPassword
                .perform(clearText())
                .perform(typeText("sekar"));
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_password))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.error_invalid_password)));

        registerPassword
                .perform(clearText(),
                        typeText("sekara"),
                        closeSoftKeyboard());
        onView(errorTkpdHintTextInputLayout(R.id.wrapper_password))
                .check(matches(not(isDisplayed())));

        onView(withId(R.id.register_button)).check(matches(isEnabled()));
    }

    /**
     * test_id : {Register/071}
     */
    @Test
    public void testRegisterWithRegisteredTokopedia() throws Exception {
        mIntentsRule.getIndexMockWebServer(SESSION_URL_ACCOUNTS_DOMAIN).enqueue(tempMockResponse("registed_with_active_tokopedia.json"));

        defaultGoogleRegister();

        startEmptyActivity();

        Thread.sleep(1000);

        allowPermissionsIfNeeded(Manifest.permission.GET_ACCOUNTS);

        fillWithCorrect();

        onView(withId(R.id.register_button)).perform(click());

        ScreenShotter.takeScreenshot("4", mIntentsRule.getActivity());
    }

    /**
     * test_id : {"Register/020","Register/021","Register/022", "Register/023"}
     */
    @Test
    public void testRegisterWithServerValidation() throws Exception {
        ErrorMessageModel tkpdResponse = dynamicResponse(
                "register_error_validation_tokopedia.json"
        );

        String customMessage = "Kata sandi harus memiliki minimum 1 huruf besar";

        overrideSingleErrorMessages(
                customMessage, tkpdResponse
        );

        mIntentsRule.getIndexMockWebServer(SESSION_URL_ACCOUNTS_DOMAIN).enqueue(tempMockResponseRaw(
                dynamicResponseToString(tkpdResponse)
        ));

        defaultGoogleRegister();

        startEmptyActivity();

        Thread.sleep(1000);

        allowPermissionsIfNeeded(Manifest.permission.GET_ACCOUNTS);

        fillWithCorrect();

        onView(withId(R.id.register_button)).perform(click());

        ScreenShotter.takeScreenshot("5", mIntentsRule.getActivity());

        snackbarMatcher(ErrorHandler.getErrorMessage(new ErrorMessageException(
                customMessage)));
    }

    /**
     * test_id : {"Register/025","Register/028","", ""}
     *
     * @throws Exception
     */
    @Test
    public void testRegisterNotActivated() throws Exception {
        RegisterModel registerModel = (RegisterModel) dynamicResponse(
                "registed_with_active_tokopedia.json", RegisterModel.class
        );

        registerModel.getData().setAction(RegisterEmailPresenterImpl.GO_TO_ACTIVATION_PAGE);
        registerModel.getData().setIsActive(RegisterEmailPresenterImpl.STATUS_INACTIVE);

        mIntentsRule.getIndexMockWebServer(SESSION_URL_ACCOUNTS_DOMAIN).enqueue(tempMockResponseRaw(
                dynamicResponseToString(registerModel, RegisterModel.class)
        ));

        registerModel.getData().setAction(RegisterEmailPresenterImpl.GO_TO_ACTIVATION_PAGE);
        registerModel.getData().setIsActive(RegisterEmailPresenterImpl.STATUS_PENDING);

        mIntentsRule.getIndexMockWebServer(SESSION_URL_ACCOUNTS_DOMAIN).enqueue(tempMockResponseRaw(
                dynamicResponseToString(registerModel, RegisterModel.class)
        ));

        ErrorMessageModel tkpdResponse = dynamicResponse(
                "register_error_validation_tokopedia.json"
        );

        String customMessage = "Nama mengandung kata tidak valid.";

        overrideSingleErrorMessages(
                customMessage, tkpdResponse
        );

        mIntentsRule.getIndexMockWebServer(SESSION_URL_ACCOUNTS_DOMAIN).enqueue(tempMockResponseRaw(
                dynamicResponseToString(tkpdResponse)
        ));

        defaultGoogleRegister();

        startEmptyActivity();

        Thread.sleep(1000);

        allowPermissionsIfNeeded(Manifest.permission.GET_ACCOUNTS);

        fillWithCorrect();

        onView(withId(R.id.register_button)).perform(click());

        Thread.sleep(1000);

        matchToolbarTitle(mIntentsRule.getActivity().getString(R.string.title_activity_activation));

        mIntentsRule.getDevice().pressBack();

        ScreenShotter.takeScreenshot("6", mIntentsRule.getActivity());

        fillWithCorrect();

        onView(withId(R.id.register_button)).perform(click());

        matchToolbarTitle(mIntentsRule.getActivity().getString(R.string.title_activity_activation));

        mIntentsRule.getDevice().pressBack();

        fillWithBlacklistName();

        onView(withId(R.id.register_button)).perform(click());

        ScreenShotter.takeScreenshot("6", mIntentsRule.getActivity());

        snackbarMatcher(ErrorHandler.getErrorMessage(new ErrorMessageException(
                customMessage)));

    }

    /**
     * Updated methode by fauzan
     */

    private void fillWithBlacklistName() {
        onView(tkpdHintTextInputLayout(R.id.wrapper_email))
                .perform(clearText())
                .perform(typeText("fauzan@gmailccc.com"));

        // blacklist name
        onView(tkpdHintTextInputLayout(R.id.wrapper_name))
                .perform(clearText())
                .perform(typeText("tokopedia"));

        onView(tkpdHintTextInputLayout(R.id.wrapper_phone))
                .perform(clearText(), replaceText("0812-8440-2240"), closeSoftKeyboard());

        onView(withId(R.id.register_password)).perform(clearText(),
                typeText("ggwp123ask"),
                closeSoftKeyboard());
    }

    private void fillWithCorrect() {
        onView(tkpdHintTextInputLayout(R.id.wrapper_email))
                .perform(clearText())
                .perform(typeText("cincin.jati+47@gmailc.com"));

        // right name
        onView(tkpdHintTextInputLayout(R.id.wrapper_name))
                .perform(clearText())
                .perform(typeText("lalalaLand"));

        onView(tkpdHintTextInputLayout(R.id.wrapper_phone))
                .perform(clearText(), replaceText("0812-8440-2240"), closeSoftKeyboard());

        onView(withId(R.id.register_password)).perform(clearText(),
                typeText("sekara"),
                closeSoftKeyboard());

    }

    private void overrideSingleErrorMessages(String errorMessage, ErrorMessageModel tkpdResponse) {
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(errorMessage);
        overrideErrorMessages(errorMessages, tkpdResponse);
    }

    private void overrideErrorMessages(List<String> errorMessages, ErrorMessageModel tkpdResponse) {
        tkpdResponse.setMessageError(errorMessages);
    }

    private String dynamicResponseToString(ErrorMessageModel tkpdResponse) {
        return CacheUtil.convertModelToString(tkpdResponse, ErrorMessageModel.class);
    }

    private String dynamicResponseToString(ErrorMessageModel tkpdResponse, Type type) {
        return CacheUtil.convertModelToString(tkpdResponse, type);
    }

    private ErrorMessageModel dynamicResponse(String filePath) {
        return CacheUtil.convertStringToModel(mIntentsRule.getBaseJsonFactory().convertFromAndroidResource(filePath), ErrorMessageModel.class);
    }

    private Object dynamicResponse(String filePath, Type type) {
        return CacheUtil.convertStringToModel(mIntentsRule.getBaseJsonFactory().convertFromAndroidResource(filePath), type);
    }

    private MockResponse tempMockResponse(String jsonResponse) {
        return mIntentsRule.getBaseJsonFactory().createSuccess200Response(jsonResponse);
    }

    private MockResponse tempMockResponseRaw(String jsonResponse) {
        return mIntentsRule.getBaseJsonFactory().createSuccess200RawResponse(jsonResponse);
    }

    private void startEmptyActivity() {
        mIntentsRule.launchActivity(null);
    }

    private void defaultGoogleRegister() {
        intending(hasComponent(GoogleSignInActivity.class.getName()))
                .respondWith(mockGoogleActivityResult("test123@gmail.com", "test123", "12345"));
    }

    private Matcher<View> tkpdHintTextInputLayout(@IdRes final int id) {
        return nthChildOf(nthChildOf(nthChildOf(
                withId(id),
                0), 1), 0);
    }

    private Matcher<View> errorTkpdHintTextInputLayout(@IdRes final int id) {
        return nthChildOf(nthChildOf(nthChildOf(nthChildOf(
                withId(id),
                0), 2), 0), 1);
    }
}
