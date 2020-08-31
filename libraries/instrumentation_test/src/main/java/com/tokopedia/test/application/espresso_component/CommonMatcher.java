package com.tokopedia.test.application.espresso_component;

import android.view.View;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;

public class CommonMatcher {
    /**
     * Matcher to find view with string tag.
     * Use this to find your view using tag, please provide unique tag for your view
     *
     * @param tagStringValue tag string value of your view
     */
    public static Matcher<View> withTagStringValue(String tagStringValue) {
        return withTagValue(Matchers.is(tagStringValue));
    }

    /**
     * Matcher to find firstView from top.
     * Use this if you are struggling with Espresso AmbiguousViewMatcherException because there is
     * multiple view with same id/tag in a viewport
     *
     * @param matcher to combine it with Matcher
     */
    public static <T> Matcher<T> firstView(final Matcher<T> matcher) {
        return new BaseMatcher<T>() {
            boolean isFirst = true;

            @Override
            public boolean matches(final Object item) {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false;
                    return true;
                }

                return false;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("should return first matching item");
            }
        };
    }
}
