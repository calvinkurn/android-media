package com.tokopedia.topchat.chatlist.viewmodel;

import android.text.Spanned;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.topchat.chatlist.adapter.InboxChatTypeFactory;
import com.tokopedia.topchat.chatlist.domain.pojo.message.Contact;

/**
 * Created by stevenfredian on 10/19/17.
 */

public class ChatListViewModel implements Visitable<InboxChatTypeFactory> {

    public static final int NO_SPAN = 0;
    public static final int SPANNED_CONTACT = 1;
    public static final int SPANNED_MESSAGE = 2;

    public static final String IS_TYPING = "sedang mengetik ...";

    String name;
    String message;
    String image;
    String time;
    String id;
    String senderId;
    String label;
    int readStatus;
    int unreadCounter;
    Spanned span;
    int spanMode;
    boolean haveTitle;
    int sectionSize;
    private String role;
    boolean isTyping;
    Contact contact;

    public ChatListViewModel() {
        spanMode = NO_SPAN;
        haveTitle = false;
        name = "";
        message = "";
        image = "";
        time = "";
        id = "";
        senderId = "";
        label = "";
        span = MethodChecker.fromHtml("");
        role = "";
    }

    private boolean isChecked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public int getUnreadCounter() {
        return unreadCounter;
    }

    public void setUnreadCounter(int counter) {
        this.unreadCounter = counter;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Spanned getSpan() {
        return span;
    }

    public void setSpan(Spanned span) {
        this.span = span;
    }

    public int getSpanMode() {
        return spanMode;
    }

    public void setSpanMode(int spanMode) {
        this.spanMode = spanMode;
    }

    public int getSectionSize() {
        return sectionSize;
    }

    public void setSectionSize(int sectionSize) {
        this.sectionSize = sectionSize;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }

    public boolean isHaveTitle() {
        return haveTitle;
    }

    public void setHaveTitle(boolean haveTitle) {
        this.haveTitle = haveTitle;
    }

    @Override
    public int type(InboxChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

}
