package com.tokopedia.groupchat.chatroom.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract;

import javax.inject.Inject;

/**
 * @author by nisie on 2/6/18.
 */

public class ChatroomPresenter extends BaseDaggerPresenter<ChatroomContract.View> implements
        ChatroomContract.Presenter {

    private static final int MAX_CHARACTER_LENGTH = 200;

    @Inject
    public ChatroomPresenter() {
    }

    @Override
    public String checkText(String replyText) {
        return replyText.replace("<", "&lt;");
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
