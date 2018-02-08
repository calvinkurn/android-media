import React, { Component } from 'react'
import { Provider } from 'react-redux'
import { StackNavigator } from 'react-navigation'
import store from '../store/Store'
import PaymentBank from '../components/pages/payment/PaymentBank'
import Payment from '../components/pages/payment/Payment'



const PaymentApp = StackNavigator({
    BankSelection : { screen: PaymentBank },
    Payment: { screen: Payment }
})

class Root extends Component {
    render() {
        return (
            <Provider store={store}>
                <PaymentApp screenProps={this.props.data} />
            </Provider>
        )
  }
}
export default Root