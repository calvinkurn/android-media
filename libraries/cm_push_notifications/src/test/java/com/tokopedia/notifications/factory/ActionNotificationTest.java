package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.notifications.R;
import com.tokopedia.notifications.model.ActionButton;
import com.tokopedia.notifications.model.BaseNotificationModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Ashwani Tyagi on 05/12/18.
 */
public class ActionNotificationTest {

    @Mock
    Context context;
    BaseNotificationModel model;
    ActionNotification actionNotification;
    ActionButton button;
    @Mock
    Notification notification;
    @Before
    public void setup(){
        model = new BaseNotificationModel();
        model.setMessage("40% discount on merchandise");
        model.setIcon("");
        model.setDetailMessage("hello world");
        model.setNotificationId(1);
        button = new ActionButton();
        button.setAppLink("abc");
        button.setText("hi");
        List<ActionButton> actions = new ArrayList<ActionButton>();
        actions.add(button);
        model.setActionButton(actions);

    }
    @Test
    public void createNotificationTest() {
       // assertNotNull(button);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context,
                0,
                new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        MockitoAnnotations.initMocks(this);
        actionNotification = Mockito.spy(new ActionNotification(context, model));
        doReturn(2).when(actionNotification).getRequestCode();
        doReturn(1010).when(actionNotification).getDrawableIcon();
        //doReturn(new Notification.Builder(context)).when(actionNotification).createMainPendingIntent("abc",2);
        doReturn(resultPendingIntent).when(actionNotification).createMainPendingIntent("xyz",2);
        assertSame(notification,actionNotification.createNotification());
    }

    @Test
    public void getNotificationTest(){

    }
}