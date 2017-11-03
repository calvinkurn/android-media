import { NetworkModule, SessionModule } from 'NativeModules'
import { 
  BASE_API_URL_PAYMENT,
  BASE_API_URL_SCROOGE,
  BASE_API_URL_PCIDSS,
  BASE_API_URL_ORDER,
  BASE_API_URL,
} from '../lib/api.js'
import { 
  getUserId, 
  getAddrId,
  getShopId,
  getEnv,
} from '../lib/utility'



//  ==================== Make Payment V2 Native ===================== //
export const MAKE_PAYMENT_V2 = 'MAKE_PAYMENT_V2'
export const makePayment = (checkout_data, installment_term, cc_no, expiry_date, cvv) => {
  const data = { checkout_data, installment_term, cc_no, expiry_date, cvv }
  console.log(data)

  return {
    type: MAKE_PAYMENT_V2,
    payload: postDataToNative(data)
  }
}

const postDataToNative = async (data) => {
  const env = await getEnv()
  const api_url = await getBaseAPI(env)
  const gateway_code = await getGatewayCode(data)
  const otp = await makePaymentV2(api_url, data, gateway_code)
  console.log(otp)
  return otp
}


const getBaseAPI = (env) => {
  let data_api = {}

  if (env === 'production'){
    const data_api = {
      api_url_payment: `${BASE_API_URL_PAYMENT.PRODUCTION}`,
      api_url_scrooge: `${BASE_API_URL_SCROOGE.PRODUCTION}`,
      api_url_pcidss: `${BASE_API_URL_PCIDSS.PRODUCTION}`,
      api_url_order: `${BASE_API_URL_ORDER.PRODUCTION}`,
      base_api_url: `${BASE_API_URL.PRODUCTION}`
    }
    return data_api

  } else {
    const data_api = {
      api_url_payment: `${BASE_API_URL_PAYMENT.STAGING}`,
      api_url_scrooge: `${BASE_API_URL_SCROOGE.STAGING}`,
      api_url_pcidss: `${BASE_API_URL_PCIDSS.STAGING}`,
      api_url_order: `${BASE_API_URL_ORDER.STAGING}`,
      base_api_url: `${BASE_API_URL.STAGING}`
    }
    return data_api
  }
}

const getGatewayCode = (data) => {
  let gtwCode = ''
  if (data.installment_term > 0 ){
    gtwCode = 'INSTALLMENT'
  } else {
    gtwCode = 'CREDITCARD'
  }
  return gtwCode
}

const makePaymentV2 = (api_url, data, gateway_code) => {
  const checkoutData = data.checkout_data.data.data
  const exp_month = (data.expiry_date).substring(0, (data.expiry_date).indexOf('/'))
  const exp_year = (data.expiry_date).substring((data.expiry_date).indexOf("/") + 1)
  const cc_card_no_without_spaces = (data.cc_no).replace(/\s/g, '')

  const data_params = {
    cc_card_no: cc_card_no_without_spaces,
    cc_expired_month: parseInt(exp_month),
    cc_expired_year: parseInt(exp_year),
    cc_cvv: parseInt(data.cvv),
    inst_term: data.installment_term,
    gateway_code: gateway_code,
    payment_amount: parseFloat(checkoutData.payment_amount),
    merchant_code: checkoutData.merchant_code,
    profile_code: checkoutData.profile_code,
    transaction_id: checkoutData.transaction_id,
    signature: checkoutData.signature
  }

  return NetworkModule.getResponseJson(`${api_url.api_url_pcidss}`, `POST`, JSON.stringify(data_params), true)
    .then(res => {
      const jsonResponse = JSON.parse(res)
      if (jsonResponse.status === '200 Ok'){
        if (jsonResponse.data.errors === null){
          return jsonResponse
        }
      }
    })
    .catch(err => { 
      console.log(err) 
      return
    })
}
//  ==================== Make Payment V2 Native ===================== //


//  ==================== Transaction History ===================== //
export const FETCH_TRANSACTION_HISTORY = 'FETCH_TRANSACTION_HISTORY'
export const getTransactionHistory = () => {
  return {
    type: FETCH_TRANSACTION_HISTORY,
    payload: fetchTransactionHistory(1) // fetch page 1
  }
}

const fetchTransactionHistory = async (page) => {
  const env = await getEnv()
  const api_url = await getBaseAPI(env)
  const user_id = await getUserId()
  const addr_id = await getAddrId()
  const data = {
    user_id: parseInt(user_id),
    addr_id: parseInt(addr_id),
    per_page: 10,
    page: page,
    os_type: '1'
  }
  const txHistory = await apiGetTransactionHistory(`${api_url.base_api_url}o2o/v1/order/get_history`, data)
  return txHistory
}

const apiGetTransactionHistory = (url, data) => {
  console.log(url)
  console.log(data)
  return NetworkModule.getResponseJson(url, `POST`, JSON.stringify(data), true)
    .then(res => {
        const jsonResponse = JSON.parse(res)
        console.log(jsonResponse)
        return jsonResponse
    })
    .catch(err => {
      console.log(err)
      return
    })
}
//  ==================== Transaction History ===================== //






