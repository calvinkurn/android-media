package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 15/01/2016.
 */
public class DepartmentParentModel {
    @SerializedName("")
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
    String[] message_error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getServer_process_time() {
        return server_process_time;
    }

    public void setServer_process_time(String server_process_time) {
        this.server_process_time = server_process_time;
    }

    public String[] getMessage_error() {
        return message_error;
    }

    public void setMessage_error(String[] message_error) {
        this.message_error = message_error;
    }

    public static class Data{
        @SerializedName("list")
        @Expose
        DepartmentParent[] list;

        public DepartmentParent[] getList() {
            return list;
        }

        public void setList(DepartmentParent[] list) {
            this.list = list;
        }
    }

    public static class DepartmentParent{
        @SerializedName("department_tree")
        @Expose
        String departmentTree;

        @SerializedName("department_id")
        @Expose
        String departmentId;

        @SerializedName("department_identifier")
        @Expose
        String departmentIdentifier;

        @SerializedName("department_dir_view")
        @Expose
        String departmentDirView;

        @SerializedName("department_name")
        @Expose
        String departmentName;

        public String getDepartmentTree() {
            return departmentTree;
        }

        public void setDepartmentTree(String departmentTree) {
            this.departmentTree = departmentTree;
        }

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }

        public String getDepartmentIdentifier() {
            return departmentIdentifier;
        }

        public void setDepartmentIdentifier(String departmentIdentifier) {
            this.departmentIdentifier = departmentIdentifier;
        }

        public String getDepartmentDirView() {
            return departmentDirView;
        }

        public void setDepartmentDirView(String departmentDirView) {
            this.departmentDirView = departmentDirView;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        @Override
        public String toString() {
            return "DepartmentParent{" +
                    "departmentTree='" + departmentTree + '\'' +
                    ", departmentId='" + departmentId + '\'' +
                    ", departmentIdentifier='" + departmentIdentifier + '\'' +
                    ", departmentDirView='" + departmentDirView + '\'' +
                    ", departmentName='" + departmentName + '\'' +
                    '}';
        }
    }
}
