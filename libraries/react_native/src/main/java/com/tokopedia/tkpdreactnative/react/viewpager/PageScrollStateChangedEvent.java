package com.tokopedia.tkpdreactnative.react.viewpager;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/**
 * Event emitted by {@link ReactViewPager} when user scrolling state changed.
 *
 * Additional data provided by this event:
 *  - pageScrollState - {Idle,Dragging,Settling}
 */
class PageScrollStateChangedEvent extends Event<PageScrollStateChangedEvent> {
    public static final String EVENT_NAME = "topPageScrollStateChanged";
    private final String mPageScrollState;

    PageScrollStateChangedEvent(int viewTag, String pageScrollState) {
        super(viewTag);
        mPageScrollState = pageScrollState;
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializedEventData());
    }

    private WritableMap serializedEventData() {
        WritableMap eventData = Arguments.createMap();
        eventData.putString("pageScrollState", mPageScrollState);
        return eventData;
    }
}
