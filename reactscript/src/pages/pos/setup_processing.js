import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from './store/Store'
import PaymentProcessing from './components/PaymentProcessing'
import PaymentInvoice from './components/PaymentInvoice'
import { StackNavigator } from 'react-navigation';



const App = StackNavigator({
    PaymentProcessing: {
        screen: PaymentProcessing,
        path: 'PaymentProcessing',
      },
    PaymentInvoice: {screen: PaymentInvoice}
}, { headerMode: 'none' });

// const prefix = 'pospayment://pospayment/'


class Root extends Component {
    componentDidMount(){
        console.log(this.props)
        // const data_process = JSON.parse(this.props.data.data_process)
        // console.log(data_process)
        // console.log(data_process.checkout_data)
    }

    render() {
        return (
            <Provider store={store}>
                <App screenProps={this.props} />
            </Provider>
        )
    }
}
export default Root

