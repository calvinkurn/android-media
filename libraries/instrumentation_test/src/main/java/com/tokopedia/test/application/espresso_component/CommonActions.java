package com.tokopedia.test.application.espresso_component;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class CommonActions {
    /**
     * Click on each item recyclerview actions.
     *
     * Best to use when:
     * You need to access all recyclerview children and you're dealing with multiple nested
     * horizontal recyclerview which causing Espresso AmbiguousViewMatcherException.
     *
     * This actions will only triggered the firstView of recyclerview inside on viewport to prevent
     * Espresso AmbiguousViewMatcherException
     *
     * @param view view object in your espresso test
     * @param recyclerViewId id of recyclerview
     * @param fixedItemPositionLimit position limit when your recyclerview is endless recyclerview
     */
    public static void clickOnEachItemRecyclerView(View view, int recyclerViewId, int fixedItemPositionLimit) {
        View childView = view;
        RecyclerView childRecyclerView = childView.findViewById(recyclerViewId);
        int childItemCount = childRecyclerView.getAdapter().getItemCount();;
        if (fixedItemPositionLimit > 0) {
            childItemCount = fixedItemPositionLimit;
        }

        for (int i = 0; i<childItemCount ; i++) {
            try {
                Espresso.onView(CommonMatcher.firstView(withId(recyclerViewId)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
            } catch (PerformException e) {
                e.printStackTrace();
            }
        }
    }
}
