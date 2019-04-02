package com.tokopedia.events.data.entity.response;

import java.util.List;

/**
 * Created by naveengoyal on 1/17/18.
 */

public class MovieSeatResponseEntity {

    private List<SeatLayoutItem> data;

    public void setData(List<SeatLayoutItem> data){
        this.data = data;
    }

    public List<SeatLayoutItem> getData(){
        return data;
    }

}
