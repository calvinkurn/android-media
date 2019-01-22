package com.tkpd.library.utils.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.database.model.NetworkModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.normansyah on 2/2/16.
 */
@Deprecated
public class ListCity {
    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("data")
    @Expose
    Data data;

    @SerializedName("config")
    @Expose
    String config;

    @SerializedName("server_process_time")
    @Expose
    String server_process_time;

    @SerializedName("message_error")
    @Expose
    List<String> messageError;

    public static class Data {
        @SerializedName("cities")
        @Expose
        ArrayList<Cities> cities;

        public ArrayList<Cities> getCities() {
            return cities;
        }

        public void setCities(ArrayList<Cities> cities) {
            this.cities = cities;
        }
    }

    public static class Cities extends NetworkModel {
        @SerializedName("city_id")
        @Expose
        String city_id;

        @SerializedName("city_name")
        @Expose
        String city_name;

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }
    }

}
