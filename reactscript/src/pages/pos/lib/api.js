export const BASE_API_URL = {
    STAGING: 'http://o2o-staging.tokopedia.com/',
    PRODUCTION: 'https://gw.tokopedia.com/'
}

export const BASE_API_URL_PAYMENT = {
    STAGING: `${BASE_API_URL.STAGING}o2o/get_payment_params`,
    PRODUCTION: `${BASE_API_URL.PRODUCTION}o2o/get_payment_params`
}

export const BASE_API_URL_SCROOGE = {
    STAGING: `${BASE_API_URL.STAGING}o2o/v1/payment/create`,
    PRODUCTION: `${BASE_API_URL.PRODUCTION}o2o/v1/payment/create`
}

export const BASE_API_URL_PCIDSS = {
    STAGING: `${BASE_API_URL.STAGING}o2o/v2/payment/process/creditcard`,
    PRODUCTION: `${BASE_API_URL.PRODUCTION}o2o/v2/payment/process/creditcard`
}

export const BASE_API_URL_ORDER = {
    STAGING: `${BASE_API_URL.STAGING}o2o/v1/order/get_history`,
    PRODUCTION: `${BASE_API_URL.PRODUCTION}o2o/v1/order/get_history`
}
