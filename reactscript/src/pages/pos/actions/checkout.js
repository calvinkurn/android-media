import { NetworkModule, PosCacheModule, SessionModule } from 'NativeModules'
import { 
  BASE_API_URL_PAYMENT,
  BASE_API_URL_SCROOGE,
  BASE_API_URL_PCIDSS
} from '../lib/api.js'


// ===================== Make Payment ===================== //
export const PAYMENT_CHECKOUT_TO_NATIVE = 'PAYMENT_CHECKOUT_TO_NATIVE'
export const PaymentCheckoutToNative = () => {
  return {
    type: PAYMENT_CHECKOUT_TO_NATIVE,
    payload: doPayment()
  }
}

const doPayment = async () => {
  const user_id = await getUserId()
  const addr_id = await getAddrId()
  const shop_id = await getShopId()
  const env = await getEnv()
  const api_url = await getBaseAPI(env)
  const local_cart = await getCart()
  
  const data_payment = {
    base_api_url_payment: api_url.api_url_payment,
    base_api_url_scrooge: api_url.api_url_scrooge,
    base_api_url_pcidss: api_url.api_url_pcidss,
    user_id: parseInt(user_id),
    os_type: '1',
    addr_id: parseInt(addr_id),
    client_id: '',
    shop_id: parseInt(shop_id),
    cart: local_cart
  }
  const paymentToNative_getParams = await makePaymentToNativeStepOne(data_payment)
  console.log(paymentToNative_getParams)
  const paymentToNative_secondStep = await makePaymentToNativeStepTwo(paymentToNative_getParams, data_payment, local_cart)
  return paymentToNative_secondStep
}

const getUserId = () => {
  return SessionModule.getUserId()
    .then(res => { return res })
    .catch(err => console.log(err))
}

const getAddrId = () => {
  return SessionModule.getAddrId()
    .then(res => { return res })
    .catch(err => console.log(err))
}

const getShopId = () => {
  return SessionModule.getShopId()
    .then(res => { return res })
    .catch(err => console.log(err))
}

const getCart = async () => {
  let objData = []
  const cart = await fetchCart()
  
  cart.list.map((data) => {
    objData.push({
      product_id: data.product_id,
      quantity: data.quantity
    })
  })

  return objData
}

const fetchCart = () => {
  return PosCacheModule.getDataAll('CART')
    .then(response => {
      const jsonResponse = JSON.parse(response)
      return jsonResponse.data;
    })
    .catch(error => console.log(error))
}

const getEnv = () => {
  return SessionModule.getEnv()
    .then(res => { return res })
    .catch(err => console.log(err))
}

const getBaseAPI = (env) => {
  let data_api = {}

  if (env === 'production'){
    const data_api = {
      api_url_payment: `${BASE_API_URL_PAYMENT.PRODUCTION}`,
      api_url_scrooge: `${BASE_API_URL_SCROOGE.PRODUCTION}`,
      api_url_pcidss: `${BASE_API_URL_PCIDSS.PRODUCTION}`
    }
    return data_api

  } else {
    const data_api = {
      api_url_payment: `${BASE_API_URL_PAYMENT.STAGING}`,
      api_url_scrooge: `${BASE_API_URL_SCROOGE.STAGING}`,
      api_url_pcidss: `${BASE_API_URL_PCIDSS.STAGING}`
    }
    return data_api
  }
}

const makePaymentToNativeStepOne = (data_payment) => {
  const data = {
    user_id: data_payment.user_id,
    os_type: data_payment.os_type,
    addr_id: data_payment.addr_id,
    client_id: data_payment.client_id,
    shop_id: data_payment.shop_id,
    cart: data_payment.cart
  }

  return NetworkModule.getResponseJson(`${data_payment.base_api_url_payment}`, `POST`, JSON.stringify(data), true)
    .then(response => {
      const jsonResponse = JSON.parse(response)
      console.log(jsonResponse)
      if (jsonResponse.data.status !== 'ERROR'){
        const itemsRes = jsonResponse.data.data.parameter.items
        let itemsData = []
  
        itemsRes.map((res) => {
          itemsData.push({ 
            id: res.id,
            name: res.name,
            price: res.price,
            quantity: res.quantity
          })
        })
        return { jsonResponse, itemsData}
      }
    })
    .catch(err => { 
      console.log(err) 
      return
    })
}
// ===================== Make Payment ===================== //


// ===================== Make Payment 2 ===================== //
const makePaymentToNativeStepTwo = (paymentToNative_getParams, data_payment, local_cart) => {
  let data_items = []
  const paymentParams = paymentToNative_getParams.itemsData

  if (!paymentParams) return 


  const data_cart = paymentToNative_getParams.jsonResponse.data.data.parameter.items || []
  data_cart.map((res) => {
    data_items.push({
      Name: res.name,
      Quantity: res.quantity,
      Price: res.price
    })
  })
  
  const dataParams = paymentToNative_getParams.jsonResponse.data.data.parameter

  const payloads = {
    merchant_code: dataParams.merchant_code,
    profile_code: dataParams.profile_code,
    transaction_id: dataParams.transaction_id,
    transaction_date: dataParams.transaction_date,
    currency: dataParams.currency,
    amount: parseFloat(dataParams.amount),
    customer_name: dataParams.customer_name,
    customer_email: dataParams.customer_email,
    user_defined_value: dataParams.user_defined_value,
    payment_metadata: dataParams.payment_meta,
    signature: dataParams.signature,
    items: data_items
  }
  const payloadString = JSON.stringify(payloads)
  
  return NetworkModule.getResponseJson(`${data_payment.base_api_url_scrooge}`, `POST`, payloadString, false)
    .then(res => {
      const jsonResponse = JSON.parse(res)
      console.log(jsonResponse)
      if (jsonResponse.status === '200 Ok'){
        if (jsonResponse.data.code === 200 && jsonResponse.data.message === 'Success'){
          return jsonResponse
        }
      }
    })
    .catch(err => {
      console.log(err)
      return 
    })
}
// ===================== Make Payment 2 ===================== //