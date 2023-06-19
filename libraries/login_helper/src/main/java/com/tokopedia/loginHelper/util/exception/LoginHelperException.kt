package com.tokopedia.loginHelper.util.exception

class GoToSecurityQuestionException :
    Exception("Account not whitelisted. Go To Security Questions got called. Please try another account.")

class ShowPopupErrorException :
    Exception("Account not whitelisted. Show Error Popup got called. Please try another account.")

class GoToActivationPageException :
    Exception("Account not whitelisted. Go to activation page got called. Please try another account")

class ShowLocationAdminPopupException :
    Exception("Account not whitelisted. Show Location Admin popup got called. Please try another account.")

class LocationAdminRedirectionException :
    Exception("Account not whitelisted. Location admin redirection got called. Please try another account.")

class ErrorGetAdminTypeException :
    Exception("Account not whitelisted. Error get admin type got called. Please try another account.")

class NoUserInfoException:
    Exception("No User details found, switch to Local")
