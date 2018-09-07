package com.tokopedia.topchat.chatroom.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;

/**
 * Created by stevenfredian on 10/26/17.
 */

public class ChatRoomViewModel {

    String nameHeader;
    String labelHeader;
    String onlineTime;
    boolean onlineStatus;
    String imageHeader;
    int textAreaReply;
    List<Visitable> chatList;
    boolean hasNext;
    private boolean hasTimeMachine;
    private int shopId;

    public String getNameHeader() {
        return nameHeader;
    }

    public void setNameHeader(String nameHeader) {
        this.nameHeader = nameHeader;
    }

    public String getLabelHeader() {
        return labelHeader;
    }

    public void setLabelHeader(String labelHeader) {
        this.labelHeader = labelHeader;
    }

    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(String imageHeader) {
        this.imageHeader = imageHeader;
    }

    public List<Visitable> getChatList() {
        return chatList;
    }

    public void setChatList(List<Visitable> chatList) {
        this.chatList = chatList;
    }

    public int getTextAreaReply() {
        return textAreaReply;
    }

    public void setTextAreaReply(int textAreaReply) {
        this.textAreaReply = textAreaReply;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasTimeMachine() {
        return hasTimeMachine;
    }

    public void setHasTimeMachine(boolean hasTimeMachine) {
        this.hasTimeMachine = hasTimeMachine;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }
}
