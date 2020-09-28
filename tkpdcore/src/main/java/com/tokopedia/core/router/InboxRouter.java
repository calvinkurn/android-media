package com.tokopedia.core.router;

/**
 * Created by Nathaniel on 11/11/2016.
 */

/**
 * @author nisie
 * @deprecated do not use this class
 * please use TkpdInboxRouter instead
 */
@Deprecated
public class InboxRouter {

    private static final String INBOX_RESCENTER_ACTIVITY = "com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity";

    public static final String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";

    private static final String INBOX_MESSAGE_ACTIVITY = "com.tokopedia.inbox.inboxmessage.activity.InboxMessageActivity";
    public static final java.lang.String PARAM_SHOP_ID = "to_shop_id";

    //Trouble ID


    /////////// INTENT

    public static Class<?> getInboxMessageActivityClass() {
        Class<?> parentIndexHomeClass = null;
        try {
            parentIndexHomeClass = getActivityClass(INBOX_MESSAGE_ACTIVITY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentIndexHomeClass;
    }

    public static Class<?> getInboxResCenterActivityClass() {
        Class<?> parentIndexHomeClass = null;
        try {
            parentIndexHomeClass = getActivityClass(INBOX_RESCENTER_ACTIVITY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentIndexHomeClass;
    }

    private static Class<?> getActivityClass(String activityFullPath) throws ClassNotFoundException {
        return Class.forName(activityFullPath);
    }
}
