package com.tokopedia.core.profile.model;

import java.util.List;

/**
 * @author by nisie on 6/19/17.
 */

public class GetUserInfoDomainData {

    private int userId;
    private String fullName;
    private String firstName;
    private String name;
    private String email;
    private int gender;
    private String bday;
    private int age;
    private String phone;
    private String phoneMasked;
    private String registerDate;
    private int status;
    private String lang;
    private boolean createdPassword;
    private boolean phoneVerified;

    private List<Integer> roles = null;
    private String profilePicture;
    private String clientId;
    private int completion;
    private List<String> createPasswordList;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhoneMasked(String phoneMasked) {
        this.phoneMasked = phoneMasked;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isCreatedPassword() {
        return createdPassword;
    }

    public void setCreatedPassword(boolean createdPassword) {
        this.createdPassword = createdPassword;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }


    public void setCreatePasswordList(List<String> createPasswordList) {
        this.createPasswordList = createPasswordList;
    }
}
