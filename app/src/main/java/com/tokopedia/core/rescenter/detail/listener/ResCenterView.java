package com.tokopedia.core.rescenter.detail.listener;

import android.app.Fragment;

/**
 * Created by hangnadi on 2/9/16.
 */
public interface ResCenterView {

    /**
     *
     * @param fragment
     * @param tag
     */
    void inflateFragment(Fragment fragment, String tag);

    void changeSolution(String resolutionID);

    void replyConversation(String resolutionID);

    void actionUpdateShippingRefNum(String resolutionID);

    void actionAcceptSolution(String resolutionID);

    void actionAcceptAdminSolution(String resolutionID);

    void actionInputShippingRefNum(String resolutionID);

    void actionFinishReturSolution(String resolutionID);

    void actionCancelResolution(String resolutionID);

    void actionReportResolution(String resolutionID);
}
