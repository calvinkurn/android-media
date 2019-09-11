package com.tokopedia.tkpd;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Checks;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.tokopedia.session.google.GoogleSignInActivity;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

import okhttp3.mockwebserver.MockResponse;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by normansyahputa on 8/22/16.
 */

public class Utils {

    // http://blog.sqisland.com/2015/05/espresso-match-toolbar-title.html
    public static ViewInteraction matchToolbarTitle(CharSequence title) {
        return onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is(title))));
    }

    private static Matcher<Object> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override
            public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }

    /**
     * Returns a matcher that matches {@link TextView}s based on text property value.
     *
     * @param stringMatcher {@link Matcher} of {@link String} with text to match
     */
    @NonNull
    public static Matcher<View> withErrorText(final Matcher<String> stringMatcher) {

        return new BoundedMatcher<View, TextView>(TextView.class) {

            @Override
            public void describeTo(final Description description) {
                description.appendText("with error text: ");
                stringMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(final TextView textView) {
                return stringMatcher.matches(textView.getError().toString());
            }
        };
    }

    public static String getString(@StringRes int stringId) {
        return InstrumentationRegistry.getTargetContext().getString(stringId);
    }


    public static Matcher<View> childAtPosition(
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

    /**
     * this snippet code only worked for lollipop or below
     *
     * @param name
     * @param activity
     */
    public static void takeScreenshot(String name, Activity activity) {
        if (Integer.valueOf(Build.VERSION.SDK) < Build.VERSION_CODES.LOLLIPOP)
            return;

        // In Testdroid Cloud, taken screenshots are always stored
        // under /test-screenshots/ folder and this ensures those screenshots
        // be shown under Test Results
        String path =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/test-screenshots/" + name + ".png";

        View scrView = activity.getWindow().getDecorView().getRootView();
        scrView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(scrView.getDrawingCache());
        scrView.setDrawingCacheEnabled(false);

        OutputStream out = null;
        File imageFile = new File(path);

        try {
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
        } catch (FileNotFoundException e) {
            // exception
            Log.e("MNORMANSYAH", e.getMessage());
        } catch (IOException e) {
            Log.e("MNORMANSYAH", e.getMessage());
            // exception
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception exc) {
                Log.e("MNORMANSYAH", exc.getMessage());
            }

        }
    }

    public static Matcher<View> withTextColor(final int color) {
        Checks.checkNotNull(color);

        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with text color: ");
            }

            @Override
            protected boolean matchesSafely(TextView item) {
                return color == item.getCurrentTextColor();
            }
        };
    }

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(-1);
    }

    public static MockResponse createSuccess200Response(String response) {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(response);
        return mockResponse;
    }

    public static MockResponse create200DelayedResponse(String response) {
        MockResponse mockResponse = new MockResponse()
                .setBodyDelay(3, SECONDS)
                .setResponseCode(200)
                .setBody(response);
        return mockResponse;
    }

    public static MockResponse create404Forbidden(String response) {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(404)
                .setBody(response);
        return mockResponse;
    }

    public static MockResponse create403Forbidden(String response) {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(403)
                .setBody(response);
        return mockResponse;
    }

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + childPosition + " child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }

    public static void snackbarMatcher(String text) {
        assert text != null;
        onView(allOf(withId(com.google.android.material.R.id.snackbar_text), withText(text)))
                .check(matches(isDisplayed()));
    }

    public static void snackbarAnyMatcher() {
        onView(allOf(withId(com.google.android.material.R.id.snackbar_text)))
                .check(matches(isDisplayed()));
    }

    public static void changeScreenOrientation(final Activity activity) {
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (Settings.System.getInt(activity.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }, 4000);
        }
    }

    public static Instrumentation.ActivityResult mockGoogleActivityResult(String email, String name, String googleAccountToken) {
        Intent resultData = new Intent();
        Bundle bundle = new Bundle();
        GoogleSignInAccount account = mock(GoogleSignInAccount.class);
        if (!TextUtils.isEmpty(email))
            when(account.getEmail()).thenReturn(email);
        if (!TextUtils.isEmpty(name))
            when(account.getDisplayName()).thenReturn(name);
        bundle.putParcelable(GoogleSignInActivity.KEY_GOOGLE_ACCOUNT, account);
        bundle.putString(GoogleSignInActivity.KEY_GOOGLE_ACCOUNT_TOKEN, googleAccountToken);

        resultData.putExtras(bundle);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    public static void allowPermissionsIfNeeded(String permissionNeeded) throws Exception {
        if (Build.VERSION.SDK_INT >= 23) {
            UiDevice device = UiDevice.getInstance(getInstrumentation());
            UiObject allowPermissions = device.findObject(new UiSelector().text("OK"));
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click();
                } catch (UiObjectNotFoundException e) {
                    Log.e(Utils.class.getName(), "There is no permissions dialog to interact with ");
                }
            }
            Thread.sleep(10000);
            PermissionGranter.allowPermissionsIfNeeded(permissionNeeded);
        }
    }

    /**
     * get private filed in this specific object
     *
     * @param targetClass
     * @param instance    the filed owner
     * @param fieldName
     * @param <T>
     * @return field if success, null otherwise.
     */
    public static <T> T getField(Class targetClass, Object instance, String fieldName) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * change the field value
     *
     * @param targetClass
     * @param instance    the filed owner
     * @param fieldName
     * @param value
     */
    public static void setField(Class targetClass, Object instance, String fieldName, Object value) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static ViewAction withCustomConstraints(final ViewAction action, final Matcher<View> constraints) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return constraints;
            }

            @Override
            public String getDescription() {
                return action.getDescription();
            }

            @Override
            public void perform(UiController uiController, View view) {
                action.perform(uiController, view);
            }
        };
    }

    public static class RecyclerViewTestUtils {
        public static <VH extends RecyclerView.ViewHolder> ViewAction actionOnItemViewAtPosition(int position,
                                                                                                 @IdRes
                                                                                                         int viewId,
                                                                                                 ViewAction viewAction) {
            return new ActionOnItemViewAtPositionViewAction(position, viewId, viewAction);
        }

        public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {

            return new RecyclerViewMatcher(recyclerViewId);
        }

        private static final class ActionOnItemViewAtPositionViewAction<VH extends RecyclerView
                .ViewHolder>
                implements

                ViewAction {
            private final int position;
            private final ViewAction viewAction;
            private final int viewId;

            private ActionOnItemViewAtPositionViewAction(int position,
                                                         @IdRes int viewId,
                                                         ViewAction viewAction) {
                this.position = position;
                this.viewAction = viewAction;
                this.viewId = viewId;
            }

            public Matcher<View> getConstraints() {
                return Matchers.allOf(new Matcher[]{
                        ViewMatchers.isAssignableFrom(RecyclerView.class), isDisplayed()
                });
            }

            public String getDescription() {
                return "actionOnItemAtPosition performing ViewAction: "
                        + this.viewAction.getDescription()
                        + " on item at position: "
                        + this.position;
            }

            public void perform(UiController uiController, View view) {
                RecyclerView recyclerView = (RecyclerView) view;
                (new ScrollToPositionViewAction(this.position)).perform(uiController, view);
                uiController.loopMainThreadUntilIdle();

                View targetView = recyclerView.getChildAt(this.position).findViewById(this.viewId);

                if (targetView == null) {
                    throw (new PerformException.Builder()).withActionDescription(this.toString())
                            .withViewDescription(

                                    HumanReadables.describe(view))
                            .withCause(new IllegalStateException(
                                    "No view with id "
                                            + this.viewId
                                            + " found at position: "
                                            + this.position))
                            .build();
                } else {
                    this.viewAction.perform(uiController, targetView);
                }
            }
        }

        private static final class ScrollToPositionViewAction implements ViewAction {
            private final int position;

            private ScrollToPositionViewAction(int position) {
                this.position = position;
            }

            public Matcher<View> getConstraints() {
                return Matchers.allOf(new Matcher[]{
                        ViewMatchers.isAssignableFrom(RecyclerView.class), isDisplayed()
                });
            }

            public String getDescription() {
                return "scroll RecyclerView to position: " + this.position;
            }

            public void perform(UiController uiController, View view) {
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.scrollToPosition(this.position);
            }
        }
    }

    public static class RecyclerViewMatcher {
        private final int recyclerViewId;

        public RecyclerViewMatcher(int recyclerViewId) {
            this.recyclerViewId = recyclerViewId;
        }

        public Matcher<View> atPosition(final int position) {
            return atPositionOnView(position, -1);
        }

        public Matcher<View> atPositionOnView(final int position, final int targetViewId) {

            return new TypeSafeMatcher<View>() {
                Resources resources = null;
                View childView;

                public void describeTo(Description description) {
                    String idDescription = Integer.toString(recyclerViewId);
                    if (this.resources != null) {
                        try {
                            idDescription = this.resources.getResourceName(recyclerViewId);
                        } catch (Resources.NotFoundException var4) {
                            idDescription = String.format("%s (resource name not found)",
                                    new Object[]{Integer.valueOf
                                            (recyclerViewId)});
                        }
                    }

                    description.appendText("with id: " + idDescription);
                }

                public boolean matchesSafely(View view) {

                    this.resources = view.getResources();

                    if (childView == null) {
                        RecyclerView recyclerView =
                                (RecyclerView) view.getRootView().findViewById(recyclerViewId);
                        if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                            childView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
                        } else {
                            return false;
                        }
                    }

                    if (targetViewId == -1) {
                        return view == childView;
                    } else {
                        View targetView = childView.findViewById(targetViewId);
                        return view == targetView;
                    }

                }
            };
        }
    }

    public static class ExtraAssertions {
        public static ViewAssertion isVisible() {
            return new ViewAssertion() {
                public void check(View view, NoMatchingViewException noView) {
                    assertThat(view, new VisibilityMatcher(View.VISIBLE));
                }
            };
        }

        public static ViewAssertion isGone() {
            return new ViewAssertion() {
                public void check(View view, NoMatchingViewException noView) {
                    assertThat(view, new VisibilityMatcher(View.GONE));
                }
            };
        }

        public static ViewAssertion isInvisible() {
            return new ViewAssertion() {
                public void check(View view, NoMatchingViewException noView) {
                    assertThat(view, new VisibilityMatcher(View.INVISIBLE));
                }
            };
        }

        private static class VisibilityMatcher extends BaseMatcher<View> {

            private int visibility;

            public VisibilityMatcher(int visibility) {
                this.visibility = visibility;
            }

            @Override
            public void describeTo(Description description) {
                String visibilityName;
                if (visibility == View.GONE) visibilityName = "GONE";
                else if (visibility == View.VISIBLE) visibilityName = "VISIBLE";
                else visibilityName = "INVISIBLE";
                description.appendText("View visibility must has equals " + visibilityName);
            }

            @Override
            public boolean matches(Object o) {

                if (o == null) {
                    if (visibility == View.GONE || visibility == View.INVISIBLE) return true;
                    else if (visibility == View.VISIBLE) return false;
                }

                if (!(o instanceof View))
                    throw new IllegalArgumentException("Object must be instance of View. Object is instance of " + o);
                return ((View) o).getVisibility() == visibility;
            }
        }
    }

    /**
     * Comparing using vector based is still buggy
     */
    public static class DrawableMatcher extends TypeSafeMatcher<View> {

        private final int expectedId;
        String resourceName;

        public DrawableMatcher(int expectedId) {
            super(View.class);
            this.expectedId = expectedId;
        }

        @Override
        protected boolean matchesSafely(View target) {
            if (!(target instanceof ImageView)) {
                return false;
            }
            ImageView imageView = (ImageView) target;
            if (expectedId < 0) {
                return imageView.getDrawable() == null;
            }
            Resources resources = target.getContext().getResources();
            Drawable expectedDrawable = resources.getDrawable(expectedId);
            resourceName = resources.getResourceEntryName(expectedId);

            if (expectedDrawable == null) {
                return false;
            }


//            if(imageView.getDrawable() instanceof BitmapDrawable &&
//                    expectedDrawable instanceof BitmapDrawable){
//                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//                Bitmap otherBitmap = ((BitmapDrawable) expectedDrawable).getBitmap();
//                return bitmap.sameAs(otherBitmap);
//            }else{
            Drawable.ConstantState bitmap = imageView.getDrawable().getConstantState();
            Drawable.ConstantState otherBitmap = expectedDrawable.getConstantState();
            return bitmap.equals(otherBitmap);
//            }
        }


        @Override
        public void describeTo(Description description) {
            description.appendText("with drawable from resource id: ");
            description.appendValue(expectedId);
            if (resourceName != null) {
                description.appendText("[");
                description.appendText(resourceName);
                description.appendText("]");
            }
        }
    }

}
