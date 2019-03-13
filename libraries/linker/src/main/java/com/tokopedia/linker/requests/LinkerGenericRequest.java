package com.tokopedia.linker.requests;

public class LinkerGenericRequest<T> {

    private int eventId;
    private T dataObj;

    public T getDataObj() {
        return dataObj;
    }

    public void setDataObj(T dataObj) {
        this.dataObj = dataObj;
    }

    public LinkerGenericRequest(int eventId, T dataObj){
        this.eventId = eventId;
        this.dataObj = dataObj;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

}
