//package com.tokopedia.util;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//public class TestLayoutInflater extends LayoutInflater{
//    private int resource;
//    private ViewGroup root;
//
//    public TestLayoutInflater(Context context) {
//        super(context);
//    }
//
//    public TestLayoutInflater(LayoutInflater original, Context newContext) {
//        super(original, newContext);
//    }
//
//    @Override
//    public LayoutInflater cloneInContext(Context newContext) {
//        return null;
//    }
//
//    @Override public View inflate(int resource, ViewGroup root) {
//        this.resource = resource;
//        this.root = root;
//        return null;
//    }
//
//    @Override public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
//        this.resource = resource;
//        this.root = root;
//        return null;
//    }
//
//    public int getResId() {
//        return resource;
//    }
//
//    public ViewGroup getRoot() {
//        return root;
//    }
//}
