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
  try {
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
    const paymentToNative_secondStep = await makePaymentToNativeStepTwo(paymentToNative_getParams, data_payment, local_cart)
    // console.log(data_payment)
    // console.log(paymentToNative_getParams)  
    // console.log(paymentToNative_secondStep)
    return paymentToNative_secondStep
  } catch (err){
    console.log(err)
    return
  }
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

  return NetworkModule.getResponseJson(`${data_payment.base_api_url_payment}/o2o/get_payment_params`, `POST`, JSON.stringify(data), true)
    .then(response => {
      const jsonResponse = JSON.parse(response)
      const itemsRes = jsonResponse.data.parameter.items
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
    })
    .catch(err => { 
      console.log(err) 
      return
    })
}
// ===================== Make Payment ===================== //


// ===================== Make Payment 2 ===================== //
const makePaymentToNativeStepTwo = (paymentToNative_getParams, data_payment, local_cart) => {
  let data_items = ''
  const paymentParams = paymentToNative_getParams.itemsData

  if (!paymentParams) return 

  paymentParams.map((res) => {
    data_items += `items[name]=${encodeURIComponent(res.name)}&items[quantity]=${encodeURIComponent(res.quantity)}&items[price]=${encodeURIComponent(res.price)}&`
  })
  
  const data_length = data_items.length
  const data_cleaned = data_items.substr(0, data_length - 1)
  const dataParams = paymentToNative_getParams.jsonResponse.data.parameter
  const data_qry_params = `merchant_code=${encodeURIComponent(dataParams.merchant_code)}&profile_code=${encodeURIComponent(dataParams.profile_code)}&` +
      `transaction_id=${encodeURIComponent(dataParams.transaction_id)}&transaction_date=${encodeURIComponent(dataParams.transaction_date)}&` +
      `currency=${encodeURIComponent(dataParams.currency)}&amount=${encodeURIComponent(dataParams.amount)}&customer_name=${encodeURIComponent(dataParams.customer_name)}&` + 
      `customer_email=${encodeURIComponent(dataParams.customer_email)}&user_defined_value=${encodeURIComponent(dataParams.user_defined_value)}&` +
      `payment_metadata=${encodeURIComponent(dataParams.payment_meta)}&signature=${encodeURIComponent(dataParams.signature)}&`
  const query_params = data_qry_params + '' + data_cleaned

  return NetworkModule.getResponseParam(`${data_payment.base_api_url_scrooge}/v1/api/payment`, `POST`, query_params, false)
    .then(res => {
      const jsonResponse = JSON.parse(res)
      return jsonResponse
    })
    .catch(err => { 
      console.log(err) 
      return
    })
}
// ===================== Make Payment 2 ===================== //