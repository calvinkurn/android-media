import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from './store/Store'
import PaymentBank from './components/PaymentBank'
import Payment from './components/Payment'
import PaymentProcessing from './components/PaymentProcessing'
import PaymentInvoice from './components/PaymentInvoice'
import TransactionHistory from './components/TransactionHistory'
import PasswordPopup from './components/PasswordPopup'
import POS from './components/POS'
import CartContainer from './containers/CartContainer'
import { StackNavigator } from 'react-navigation'


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