/*
 * Copyright 2019 Tokopedia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by Hendy
 * Edited by Meyta
 */

package com.tokopedia.coachmark;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ScrollView;

import com.tokopedia.coachmark.util.ViewHelper;

import java.util.ArrayList;


public class CoachMark extends DialogFragment {

    private static final String ARG_BUILDER = "BUILDER";
    public static final int DELAY_SCROLLING = 350;
    public static final String TAG = CoachMark.class.getSimpleName();
    public static final int MAX_RETRY_LAYOUT = 3;

    private ArrayList<CoachMarkItem> tutorsList;
    private int currentTutorIndex = -1;
    private CoachMarkBuilder builder;
    private String tag;

    boolean hasViewGroupHandled = false;

    private OnShowCaseStepListener listener;

    private int retryCounter = 0;

    public interface OnShowCaseStepListener {
        /**
         * @param previousStep
         * @param nextStep
         * @param coachMarkItem
         * @return true if already fully handled show case step inthis function
         */
        boolean onShowCaseGoTo(int previousStep, int nextStep, CoachMarkItem coachMarkItem);
    }

    public void setShowCaseStepListener(OnShowCaseStepListener listener) {
        this.listener = listener;
    }

    static CoachMark newInstance(CoachMarkBuilder builder) {
        final Bundle args = new Bundle();
        final CoachMark fragment = new CoachMark();
        args.putParcelable(ARG_BUILDER, builder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgs(getArguments());
        setRetainInstance(true);
    }

    private void getArgs(Bundle args) {
        builder = (CoachMarkBuilder) args.get(ARG_BUILDER);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.CoachMark) {
            @Override
            public void onBackPressed() {
                previous();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final CoachMarkLayout view = new CoachMarkLayout(getActivity(), builder);
        initViews(view);
        return view;
    }

    private void initViews(CoachMarkLayout view) {
        view.setShowCaseListener(new CoachMarkListener() {
            @Override
            public void onPrevious() {
                previous();
            }

            @Override
            public void onNext() {
                next();
            }

            @Override
            public void onComplete() {
                if (!TextUtils.isEmpty(tag)) {
                    CoachMarkPreference.setShown(getActivity(), tag, true);
                }
                CoachMark.this.close();
            }
        });

        setCancelable(true);
    }

    public void next() {
        if ((currentTutorIndex + 1) >= tutorsList.size()) {
            this.close();
        } else {
            CoachMark.this.show(getActivity(), tag, tutorsList, currentTutorIndex + 1);
        }
    }

    public void previous() {
        if ((currentTutorIndex - 1) < 0) {
            currentTutorIndex = 0;
        } else {
            CoachMark.this.show(getActivity(), tag, tutorsList, currentTutorIndex - 1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        final Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setDimAmount(0f);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public boolean hasShown(Activity activity, String tag) {
        return CoachMarkPreference.hasShown(activity, tag);
    }

    public void show(Activity activity, @Nullable String tag, final ArrayList<CoachMarkItem> tutorList) {
        show(activity, tag, tutorList, 0);
    }

    public void show(final Activity activity, @Nullable String tag, final ArrayList<CoachMarkItem> tutorList, int indexToShow) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        try {
            this.tutorsList = tutorList;
            this.tag = tag;
            if (indexToShow < 0 || indexToShow >= tutorList.size()) {
                indexToShow = 0;
            }
            int previousIndex = currentTutorIndex;
            currentTutorIndex = indexToShow;

            hasViewGroupHandled = false;
            if (listener != null) {
                hasViewGroupHandled = listener.onShowCaseGoTo(previousIndex, currentTutorIndex, tutorList.get(currentTutorIndex));
            }

            // has been handled by listener
            if (hasViewGroupHandled) return;

            final CoachMarkItem coachMarkItem = tutorList.get(currentTutorIndex);
            final ViewGroup viewGroup = coachMarkItem.getScrollView();
            if (viewGroup != null) {
                final View viewToFocus = coachMarkItem.getView();
                if (viewToFocus != null) {
                    hideLayout();
                    viewGroup.post(new Runnable() {
                        @Override
                        public void run() {
                            if (viewGroup instanceof ScrollView) {
                                ScrollView scrollView = (ScrollView) viewGroup;
                                int relativeLocation[] = new int[2];
                                ViewHelper.getRelativePositionRec(viewToFocus, viewGroup, relativeLocation);
                                scrollView.smoothScrollTo(0, relativeLocation[1]);
                                scrollView.postDelayed(() -> showLayout(activity, coachMarkItem), DELAY_SCROLLING);
                            } else if (viewGroup instanceof NestedScrollView) {
                                NestedScrollView scrollView = (NestedScrollView) viewGroup;
                                int relativeLocation[] = new int[2];
                                ViewHelper.getRelativePositionRec(viewToFocus, viewGroup, relativeLocation);
                                scrollView.smoothScrollTo(0, relativeLocation[1]);
                                scrollView.postDelayed(() -> showLayout(activity, coachMarkItem), DELAY_SCROLLING);
                            }
                        }
                    });
                    hasViewGroupHandled = true;
                } else {
                    hasViewGroupHandled = false;
                }
            }

            if (!hasViewGroupHandled) {
                showLayout(activity, tutorsList.get(currentTutorIndex));
            }
        } catch (Exception e) {
            // to Handle the unknown exception.
            // Since this only for first guide, if any error appears, just don't show the guide
            try {
                CoachMark.this.dismiss();
            } catch (Exception e2) {
                // no op
            }
        }
    }

    public void showLayout(Activity activity, CoachMarkItem coachMarkItem) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        FragmentManager fm = activity.getFragmentManager();
        if (!isVisible()) {
            try {
                if (!isAdded()) {
                    show(fm, TAG);
                } else if (isHidden()) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.show(CoachMark.this);
                    ft.commit();
                }
            } catch (IllegalStateException e) {
                // called in illegal state. just return.
                return;
            }
        }

        final View view = coachMarkItem.getView();
        final String title = coachMarkItem.getTitle();
        final String text = coachMarkItem.getDescription();
        final CoachMarkContentPosition coachMarkContentPosition = coachMarkItem.getCoachMarkContentPosition();
        final int tintBackgroundColor = coachMarkItem.getTintBackgroundColor();
        final int[] location = coachMarkItem.getLocation();
        final int radius = coachMarkItem.getRadius();

        if (view == null) {
            layoutShowTutorial(null, title, text, coachMarkContentPosition,
                    tintBackgroundColor, location, radius);
        } else {
            view.post(new Runnable() {
                @Override
                public void run() {
                    layoutShowTutorial(view, title, text, coachMarkContentPosition,
                            tintBackgroundColor, location, radius);
                }
            });
        }
    }

    public void hideLayout() {
        final CoachMarkLayout layout = (CoachMarkLayout) CoachMark.this.getView();
        if (layout == null) {
            return;
        }
        layout.hideTutorial();
    }

    private void layoutShowTutorial(final View view, final String title, final String text,
                                    final CoachMarkContentPosition coachMarkContentPosition,
                                    final int tintBackgroundColor, final int[] customTarget, final int radius) {

        try {
            final CoachMarkLayout layout = (CoachMarkLayout) CoachMark.this.getView();
            if (layout == null) {
                if (retryCounter >= MAX_RETRY_LAYOUT) {
                    retryCounter = 0;
                    return;
                }
                // wait until the layout is ready, and call itself
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        retryCounter++;
                        layoutShowTutorial(view, title, text,
                                coachMarkContentPosition, tintBackgroundColor, customTarget, radius);
                    }
                }, 1000);
                return;
            }
            retryCounter = 0;
            layout.showTutorial(view, title, text, currentTutorIndex, tutorsList.size(),
                    coachMarkContentPosition, tintBackgroundColor, customTarget, radius);
        } catch (Throwable t) {
            // do nothing
        }
    }

    public void close() {
        try {
            dismiss();
            final CoachMarkLayout layout = (CoachMarkLayout) CoachMark.this.getView();
            if (layout == null) {
                return;
            }
            layout.closeTutorial();
        } catch (Exception e) {
            // no op
        }
    }
}
